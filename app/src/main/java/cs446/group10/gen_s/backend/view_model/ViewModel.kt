package cs446.group10.gen_s.backend.view_model

import IdManager
import ViewModelHelper
import android.content.Context
import cs446.group10.gen_s.backend.model.Model
import androidx.lifecycle.ViewModel
import cs446.group10.gen_s.backend.dataClasses.*
import cs446.group10.gen_s.backend.model.IView
import cs446.group10.gen_s.backend.techniques.Technique
import cs446.group10.gen_s.backend.techniques.TechniqueFactory
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.min

object ViewModel {

    private val _openSpaces: MutableList<Space> = mutableListOf()
    private val _model: Model = Model()
    private val _dateToEpochFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun registerView(view: IView) {
        _model.addView(view)
    }

    fun init(context: Context) {
        // Add context to the model
        _model.setContext(context)

//        loadInitialData()

        // Load from storage
        _model.loadCalendarFromStorage(context)
    }

    fun loadInitialData() {
        _model.addEvent(generateEvent("Event 1",
            dateTimeToEpoch("2023-02-06 04:00"),
            dateTimeToEpoch("2023-02-06 05:00"), null))
        _model.addEvent(generateEvent("Event 2",
            dateTimeToEpoch("2023-02-06 12:00"),
            dateTimeToEpoch("2023-02-06 14:00"), null))
        _model.addEvent(generateEvent("Event 3",
            dateTimeToEpoch("2023-02-07 10:00"),
            dateTimeToEpoch("2023-02-07 12:30"), null))
        _model.addEvent(generateEvent("Event 4",
            dateTimeToEpoch("2023-03-03 07:00"),
            dateTimeToEpoch("2023-03-03 12:41"), null))
        _model.addEvent(generateEvent("Event 5",
            dateTimeToEpoch("2023-03-03 13:00"),
            dateTimeToEpoch("2023-03-03 15:00"), null))

        addPlanToCalendar(
            "Study Plan 1",
            listOf(
                Preference("Class 1",
                    LocalDateTime.of(2023, 3, 10, 10, 0).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(2023, 3, 10, 18, 0).toEpochSecond(ZoneOffset.UTC),
                    3600
                ),
                Preference("Class 2",
                    LocalDateTime.of(2023, 3, 11, 16, 0).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(2023, 3, 11, 18, 0).toEpochSecond(ZoneOffset.UTC),
                    5600
                ),
                Preference("Class 3",
                    LocalDateTime.of(2023, 3, 12, 10, 0).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(2023, 3, 12, 18, 0).toEpochSecond(ZoneOffset.UTC),
                    1800
                ),
                Preference("Class 3",
                    LocalDateTime.of(2023, 3, 13, 9, 0).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(2023, 3, 13, 10, 0).toEpochSecond(ZoneOffset.UTC),
                    3600
                ),
            ),
            LocalDateTime.of(2023, 3, 10, 8, 0).toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.of(2023, 3, 14, 18, 0).toEpochSecond(ZoneOffset.UTC),
            "#1BBA9B"
        )

        _model.addEvent(generateEvent("Event 6",
            dateTimeToEpoch("2023-03-04 10:00"),
            dateTimeToEpoch("2023-03-04 10:30"), null))
        _model.addEvent(generateEvent("Event 7",
            dateTimeToEpoch("2023-03-05 14:00"),
            dateTimeToEpoch("2023-03-05 16:00"), null))
        _model.addEvent(generateEvent("Event 8",
            dateTimeToEpoch("2023-03-06 10:00"),
            dateTimeToEpoch("2023-03-06 18:00"), null))
        _model.addEvent(generateEvent("Event 9",
            dateTimeToEpoch("2023-03-06 19:00"),
            dateTimeToEpoch("2023-03-06 20:00"), null))

        addPlanToCalendar(
            "Study Plan 2",
            listOf(
                Preference("Class 4",
                    LocalDateTime.of(2023, 3, 16, 10, 0).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(2023, 3, 16, 18, 0).toEpochSecond(ZoneOffset.UTC),
                    3600
                ),
                Preference("Class 5",
                    LocalDateTime.of(2023, 3, 16, 16, 0).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(2023, 3, 16, 18, 0).toEpochSecond(ZoneOffset.UTC),
                    5600
                ),
                Preference("Class 6",
                    LocalDateTime.of(2023, 3, 17, 10, 0).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(2023, 3, 17, 18, 0).toEpochSecond(ZoneOffset.UTC),
                    1800
                ),
                Preference("Class 7",
                    LocalDateTime.of(2023, 3, 18, 9, 0).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(2023, 3, 18, 10, 0).toEpochSecond(ZoneOffset.UTC),
                    3600
                ),
            ),
            LocalDateTime.of(2023, 3, 16, 8, 0).toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.of(2023, 3, 18, 18, 0).toEpochSecond(ZoneOffset.UTC),
            "#EF233D"
        )

        _model.addEvent(generateEvent("Event 10",
            dateTimeToEpoch("2023-03-07 10:00"),
            dateTimeToEpoch("2023-03-07 12:00"), null))
        _model.addEvent(generateEvent("Event 11",
            dateTimeToEpoch("2023-03-07 12:30"),
            dateTimeToEpoch("2023-03-07 16:00"), null))
        _model.addEvent(generateEvent("Event 12",
            dateTimeToEpoch("2023-03-07 16:00"),
            dateTimeToEpoch("2023-03-07 17:00"), null))
        _model.addEvent(generateEvent("Event 13",
            dateTimeToEpoch("2023-04-05 08:00"),
            dateTimeToEpoch("2023-04-05 08:15"), null))
        _model.addEvent(generateEvent("Event 14",
            dateTimeToEpoch("2023-04-05 12:00"),
            dateTimeToEpoch("2023-04-05 13:15"), null))
    }

    fun does() {
        // Add some plans to the calendar
    }

    private fun getExistingEvents(startRange: Long, endRange: Long): List<Event> {
        return getAllEvents().filter { event: Event ->
            event.startDate in startRange..endRange
        }
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

    private fun allocationAlgorithm(
        preferences: List<Preference>,
        startRange: Long,
        endRange: Long,
        planId: String,
        color: String? = null
    ): List<Event>? {
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
                        preference.notification,
                        planId,
                        color
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
                        preference.notification,
                        planId,
                        color
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

    private fun getStartAndEnd(events: List<Event>): Pair<Long, Long> {
        var start = Long.MAX_VALUE
        var end = 0L
        events.forEach { event ->
            start = min(start, event.startDate)
            end = max(end, event.endDate)
        }
        return Pair(start, end)
    }

    private fun generatePlan(
        planName: String,
        preferences: List<Preference>,
        startRange: Long,
        endRange: Long,
        color: String? = null
    ): Plan? {
        if (preferences.isEmpty()) {
            return null
        }
        val planId: String = IdManager.generateId()
        val newEvents: List<Event> = allocationAlgorithm(preferences, startRange, endRange, planId, color)
            ?:
            return null
        val sanitizedName: String = ViewModelHelper.sanitizePlanName(planName)
        val startEndPair: Pair<Long, Long> = getStartAndEnd(newEvents)
        return Plan(planId, sanitizedName, startEndPair.first, startEndPair.second, newEvents as MutableList<Event>, color)
    }

    fun addPlanToCalendar(
        planName: String,
        preferences: List<Preference>,
        startRange: Long,
        endRange: Long,
        color: String? = null
    ): Plan? {
        val plan: Plan = generatePlan(planName, preferences, startRange, endRange, color)
            ?:
            return null
        _model.addPlan(plan!!)
        return plan;
    }

    private fun generateTechniquePlan(
        planName: String,
        technique: Technique,
        startRange: Long,
        endRange: Long,
        dayRestriction: Pair<LocalTime, LocalTime>,
        color: String
    ): Plan? {
        val events: List<Event> = TechniqueFactory.generateEvents(
            technique,
            startRange,
            endRange,
            dayRestriction,
            getExistingEvents(startRange, endRange)
        ) ?: return null
        val planId: String = IdManager.generateId()
        events.forEach { event ->
            event.color = color
            event.planId = planId
        }
        val sanitizedName: String = ViewModelHelper.sanitizePlanName(planName)
        val startEndPair: Pair<Long, Long> = getStartAndEnd(events)
        return Plan(planId, sanitizedName, startEndPair.first, startEndPair.second, events as MutableList<Event>, color)

    }

    fun addTechniquePlanToCalendar(
        planName: String,
        technique: Technique,
        startRange: Long,
        endRange: Long,
        dayRestriction: Pair<LocalTime, LocalTime>,
        color: String
    ): Plan? {
        val plan: Plan = generateTechniquePlan(planName, technique, startRange, endRange, dayRestriction, color)
            ?: return null
        _model.addPlan(plan)
        return plan
    }

    private fun generateEvent(
        name: String,
        startDate: Long,
        endDate: Long,
        notification: Long?,
        planId: String? = null,
        color: String? = null
    ): Event {
        val eventId: String = IdManager.generateId()
        if (color != null)
            return Event(eventId, name, startDate, endDate, notification, planId, color)
        return Event(eventId, name, startDate, endDate, notification, planId)
    }

    fun addEventToCalendar(name: String, startDate: Long, endDate: Long, notification: Long?, planId: String? = null): Boolean {
        val event: Event = generateEvent(name, startDate, endDate, notification)
        return _model.addEvent(event, planId)
    }

    fun getAllEvents(): List<Event> {
        return _model.getEventsData() ?: return listOf()
    }

    fun getAllPlans(): List<Plan> {
        return _model.getPlansData()
    }

    fun getEventById(eventId: String): Event? {
        return _model.getEventById(eventId)
    }

    fun deleteCalendar() {
        _model.deleteCalendar()
    }

    fun updateEventInCalendar(eventId: String, name: String, startDate: Long, endDate: Long, notification: Long?, planId: String? = null): Boolean {
        val updatedEvent = Event("holder", name, startDate, endDate, notification)
        return _model.updateEvent(eventId, updatedEvent, planId)
    }

    fun deleteEventInCalendar(eventId: String): Boolean {
        return _model.deleteEvent(eventId)
    }

    fun getPlanName(planId: String?): String {
        if (planId == null) return "None"
        return _model.getPlanName(planId) ?: "None"
    }

    private fun dateTimeToEpoch(dateTimeStr: String): Long {
        val dateTime: LocalDateTime = LocalDateTime.parse(dateTimeStr, _dateToEpochFormatter)
        return dateTime.toEpochSecond(ZoneOffset.UTC)
    }

}