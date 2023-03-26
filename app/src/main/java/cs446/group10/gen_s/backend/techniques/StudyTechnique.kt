package cs446.group10.gen_s.backend.techniques

import IdManager
import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.dataClasses.Space

interface StudyTechnique {

    fun generateEvents(startRange: Long, endRange: Long, existingEvents: List<Event>): List<Event>? {
        // Get open spaces
        val openSpaces: MutableList<Space> = mutableListOf()
        val spaceSize: Long = studyDuration() + breakDuration()
        if (existingEvents.isNotEmpty()) {
            var numSpaces = (existingEvents[0].startDate - startRange) / spaceSize
            for (i in 0 until numSpaces)
                openSpaces.add(Space(startRange + spaceSize*i, startRange + spaceSize*(i + 1)))

            // Find all open spaces with 30 or more minutes of space
            for (i in 1 until existingEvents.size) {
                numSpaces = (existingEvents[i].startDate - existingEvents[i - 1].endDate) / spaceSize
                for (open in 0 until numSpaces)
                    openSpaces.add(Space(existingEvents[i - 1].endDate + spaceSize * (open), existingEvents[i - 1].endDate * spaceSize * (open + 1)))
            }

            numSpaces = (endRange - existingEvents.last().endDate) / spaceSize
            for (i in 0 until numSpaces)
                openSpaces.add(Space(existingEvents.last().endDate + spaceSize*i, existingEvents.last().endDate + spaceSize*(i + 1)))

        } else {
            val numSpaces = (endRange - startRange) / spaceSize
            for (i in 0 until numSpaces)
                openSpaces.add(Space(startRange + (spaceSize * i), endRange + (spaceSize * (i + 1))))
        }

        openSpaces.sortBy { it.start }
        val newEvents: MutableList<Event> = mutableListOf()
        var numFound = 0
        for (space in openSpaces) {
            val eventOne = Event(IdManager.generateId(), "${technique()} Study ${numFound + 1}", space.start, space.start + studyDuration())
            val eventTwo = Event(IdManager.generateId(), "${technique()} Break ${numFound + 1}", space.start + studyDuration(), space.end)
            newEvents.add(eventOne)
            newEvents.add(eventTwo)
            numFound += 1
        }

        if (numFound != repetitions()) {
            // Invalid generation
            null
        }
        return newEvents
    }

    fun repetitions(): Int

    fun studyDuration(): Long

    fun breakDuration(): Long

    fun technique(): String
}