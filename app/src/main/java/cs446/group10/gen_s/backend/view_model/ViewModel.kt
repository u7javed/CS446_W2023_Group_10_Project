package cs446.group10.gen_s.backend.view_model

import IdManager
import ViewModelHelper
import cs446.group10.gen_s.backend.model.Model
import androidx.lifecycle.ViewModel
import cs446.group10.gen_s.backend.dataClasses.*
import cs446.group10.gen_s.backend.model.IView

object ViewModel {

    private val _openSpaces: MutableList<Space> = mutableListOf()
    private val _model: Model = Model()

    fun registerView(view: IView) {
        _model.addView(view)
    }

    private fun getExistingEvents(startRange: Long, endRange: Long): List<Event> {
        // TODO: Determine a way to obtain existing events with a start date within the
        // TODO: range of the startRange and endRange
        val mock: List<Event> = listOf(
            Event("1", "event1", 1677916800, 1677920400),
            Event("2", "event2", 1677931200, 1677938400),
            Event("3", "event3", 1677940200, 1677942000),
            Event("4", "event4", 1677956400, 1677960000),
        )
        return mock
    }

    /**
     * Given an index, puts in the correct sorted position which, by the
     * algorithm, is in decreasing order of interval size
     */
    private fun repositionSpaces(index: Int) {
        if (index >= _openSpaces.size) return
        while (index > 0) {
            // If the space at index is in the correct position, stop
            if (_openSpaces[index].duration >= _openSpaces[index - 1].duration)
                break
            val temp: Space = _openSpaces[index]
            _openSpaces[index] = _openSpaces[index - 1]
            _openSpaces[index - 1] = temp
        }
    }

    private fun allocationAlgorithm(preferences: List<Preference>, startRange: Long, endRange: Long): List<Event>? {
        val existingEvents: List<Event> = getExistingEvents(startRange, endRange)

        // Get the list of open spaces
        _openSpaces.clear()
        if (existingEvents.isNotEmpty()) {
            if (existingEvents[0].startDate - startRange > 0)
                _openSpaces.add(Space(startRange, existingEvents[0].startDate))

            // Go through each of the existing events and find open spaces between them
            for (i in 1 until existingEvents.size) {
                if (existingEvents[i].startDate - existingEvents[i - 1].endDate > 0)
                    _openSpaces.add(Space(existingEvents[i - 1].endDate, existingEvents[i].startDate))
            }

            // Check if the end of the last existing event and the end of the preference range contain an open space
            if (endRange - existingEvents.last().endDate > 0)
                _openSpaces.add(Space(existingEvents.last().endDate, endRange))
        } else {
            // If there are no existing events within the time range, then the entire time range is an open space
            _openSpaces.add(Space(startRange, endRange))
        }

        // Sort the open spaces by smallest interval of open space to largest
        _openSpaces.sortBy { it.duration }

        // Sort the preferences by largest duration to smallest
        preferences.sortedByDescending { it.duration }

        val newEvents: MutableList<Event> = mutableListOf()
        preferences.forEach { preference ->
            var newEvent: Event? = null
            for (index in 0 until _openSpaces.size) {
                val space: Space = _openSpaces[index]
                // Situation 1: If the space starts after preference start and space ends before preference ends, then
                // check if the space is large enough
                val situation1 = (space.start >= preference.startRange &&
                        space.end <= preference.endRange && space.duration >= preference.duration)

                // Situation 2: If the space starts after the preference start and the space ends after the preference
                // ends, then check if the start of the space and end of the preference are large enough
                val situation2 = (space.start >= preference.startRange &&
                        space.end > preference.endRange && (preference.endRange - space.start) >= preference.duration)

                if (situation1 || situation2) {
                    // Create an event from space start to space start + preference duration
                    newEvent = generateEvent(
                        preference.name,
                        space.start,
                        space.start + preference.duration,
                        preference.notification
                    )

                    // If the entire open space is used, then delete the open space
                    if (newEvent.endDate >= space.end)
                        _openSpaces.removeAt(index)
                    // If there is still space available, shrink it to fit with the new space and reposition item
                    else {
                        _openSpaces[index] = Space(newEvent.endDate, space.end)
                        repositionSpaces(index)
                    }
                    break
                }

                // Situation 3: If the open space starts after the preference start and the open space ends before the
                // preference start
                val situation3 = (space.start < preference.startRange &&
                        space.end <= preference.endRange && (space.end - preference.startRange >= preference.duration))

                // Situation 4: If the open space starts before the preference start and the open space ends after the
                // preference end, then that space has enough for this preference by definition
                val situation4 = (space.start < preference.startRange && space.end > preference.endRange)

                if (situation3 || situation4) {
                    // The event starts from the preference start and goes until preference start + duration
                    newEvent = generateEvent(
                        preference.name,
                        preference.startRange,
                        preference.startRange + preference.duration,
                        preference.notification
                    )

                    // Check for potentially two spaces where space.start to preference.start may be a valid space and
                    // newEvent.end to space.end may be a second new space.
                    var numSpaces = 0
                    if (newEvent.startDate - space.start > 0) {
                        _openSpaces[index] = Space(space.start, newEvent.startDate)
                        repositionSpaces(index)
                        numSpaces++
                    }
                    if (space.end - newEvent.endDate > 0) {
                        if (numSpaces == 0) // The previous space was not found
                            _openSpaces[index] = Space(newEvent.endDate, space.end)
                        else
                            _openSpaces.add(index, Space(newEvent.endDate, space.end))
                        repositionSpaces(index)
                        numSpaces++
                    }
                    // If no new spaces exist, then delete the existing space that was used to create the event
                    if (numSpaces == 0)
                        _openSpaces.removeAt(index)
                    break
                }
            }
            if (newEvent == null) // No valid matching was found for a given preference
                return null
            newEvents.add(newEvent)
        }
        return newEvents
    }

    fun generatePlan(
        planName: String,
        preferences: List<Preference>,
        startRange: Long,
        endRange: Long
    ): Plan? {
        val newEvents: List<Event>? = allocationAlgorithm(preferences, startRange, endRange)
        if (newEvents == null) {
            // TODO: Specify or throw an error via UI or other means
            return null
        }
        val planId: String = IdManager.generateId()
        val sanitizedName: String = ViewModelHelper.sanitizePlanName(planName)
        return Plan(planId, sanitizedName, newEvents as MutableList<Event>)
    }

    fun addPlanToCalendar(
        planName: String,
        preferences: List<Preference>,
        startRange: Long,
        endRange: Long
    ) {
        val plan: Plan? = generatePlan(planName, preferences, startRange, endRange)
        if (plan == null) {
            // TODO: return an error message or display an error
        }
        _model.addPlan(plan!!)
    }

    private fun generateEvent(name: String, startDate: Long, endDate: Long, notification: Long?): Event {
        val eventId: String = IdManager.generateId()
        return Event(eventId, name, startDate, endDate, notification)
    }

    fun addEventToCalendar(name: String, startDate: Long, endDate: Long, notification: Long?): Boolean {
        val event: Event = generateEvent(name, startDate, endDate, notification)
        return _model.addEvent(event)
    }

    fun getAllEvents(): List<Event> {
        return _model.getEventsData() ?: return listOf()
    }

    fun getEventById(eventId: String): Event? {
        return _model.getEventById(eventId)
    }

}