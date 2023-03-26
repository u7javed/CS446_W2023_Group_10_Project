package cs446.group10.gen_s.backend.techniques

import cs446.group10.gen_s.backend.dataClasses.Event
import java.time.LocalTime

object TechniqueFactory {

    fun generateEvents(
        technique: Technique,
        startRange: Long,
        endRange: Long,
        dayRestriction: Pair<LocalTime, LocalTime>,
        existingEvent: List<Event>
    ): List<Event>? {
        return when (technique) {
            Technique.Pomodoro -> Pomodoro.techniqueAlgorithm(startRange, endRange, existingEvent, dayRestriction)
            Technique.Animedoro -> Animedoro.techniqueAlgorithm(startRange, endRange, existingEvent, dayRestriction)
            Technique.FlowtimeA -> FlowtimeA.techniqueAlgorithm(startRange, endRange, existingEvent, dayRestriction)
            Technique.FlowtimeB -> FlowtimeB.techniqueAlgorithm(startRange, endRange, existingEvent, dayRestriction)
            Technique.FlowtimeC -> FlowtimeC.techniqueAlgorithm(startRange, endRange, existingEvent, dayRestriction)
            Technique.FiftyTwoSeventeen -> FiftyTwoSeventeen.techniqueAlgorithm(startRange, endRange, existingEvent, dayRestriction)
        }
    }

}