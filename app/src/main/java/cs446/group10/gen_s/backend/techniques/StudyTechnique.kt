package cs446.group10.gen_s.backend.techniques

import IdManager
import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.dataClasses.Space
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

interface StudyTechnique {

    fun techniqueAlgorithm(
        startRange: Long,
        endRange: Long,
        existingEvents: List<Event>,
        dayRestriction: Pair<LocalTime, LocalTime>
    ): List<Event>? {
        // Get open spaces
        val openSpaces: MutableList<Space> = mutableListOf()
        val spaceSize: Long = studyDuration() + breakDuration()
        if (existingEvents.isNotEmpty()) {
            var numSpaces = (existingEvents[0].startDate - startRange) / spaceSize
            for (i in 0 until numSpaces) {
                val start = startRange + (spaceSize * i)
                val end = startRange + (spaceSize * (i + 1))
                if (validSpace(start, end, dayRestriction))
                    openSpaces.add(Space(start, end))
            }
            // Find all open spaces with enough minutes of space
            for (i in 1 until existingEvents.size) {
                numSpaces = (existingEvents[i].startDate - existingEvents[i - 1].endDate) / spaceSize
                for (open in 0 until numSpaces){
                    val start = existingEvents[i - 1].endDate + (spaceSize * open)
                    val end = existingEvents[i - 1].endDate + (spaceSize * (open + 1))
                    if (validSpace(start, end, dayRestriction))
                        openSpaces.add(Space(start, end))
                }
            }

            numSpaces = (endRange - existingEvents.last().endDate) / spaceSize
            for (i in 0 until numSpaces) {
                val start = existingEvents.last().endDate + (spaceSize * i)
                val end = existingEvents.last().endDate + (spaceSize * (i + 1))
                if (validSpace(start, end, dayRestriction))
                    openSpaces.add(Space(start, end))
            }
        } else {
            val numSpaces = (endRange - startRange) / spaceSize
            for (i in 0 until numSpaces) {
                val start = startRange + (spaceSize * i)
                val end = startRange + (spaceSize * (i + 1))
                if (validSpace(start, end, dayRestriction))
                    openSpaces.add(Space(start, end))
            }
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
            if (numFound == repetitions())
                break
        }

        // TODO: Uncomment this if we want to only generate plan if it fills entirety
//        if (numFound != repetitions()) {
//            // Invalid generation
//            return null
//        }
        return newEvents
    }

    fun validSpace(start: Long, end: Long, dayRestriction: Pair<LocalTime, LocalTime>): Boolean {
        val startDate = LocalDateTime.ofEpochSecond(start, 0, ZoneOffset.UTC)
        val earliest = LocalDateTime.of(startDate.toLocalDate(), dayRestriction.first)
            .toEpochSecond(ZoneOffset.UTC)
        val latest = LocalDateTime.of(startDate.toLocalDate(), dayRestriction.second)
            .toEpochSecond(ZoneOffset.UTC)

        if (start >= earliest && end <= latest)
            return true
        return false
    }

    fun repetitions(): Int

    fun studyDuration(): Long

    fun breakDuration(): Long

    fun technique(): String
}