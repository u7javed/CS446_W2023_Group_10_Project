package cs446.group10.gen_s.backend.techniques

import cs446.group10.gen_s.backend.dataClasses.Event

object TechniqueFactory {

    fun generateEvents(technique: Technique, startRange: Long, endRange: Long, existingEvent: List<Event>): List<Event>? {
        return when (technique) {
            Technique.Pomodoro -> Pomodoro.techniqueAlgorithm(startRange, endRange, existingEvent)
            Technique.Animedoro -> Animedoro.techniqueAlgorithm(startRange, endRange, existingEvent)
            Technique.FlowtimeA -> FlowtimeA.techniqueAlgorithm(startRange, endRange, existingEvent)
            Technique.FlowtimeB -> FlowtimeB.techniqueAlgorithm(startRange, endRange, existingEvent)
            Technique.FlowtimeC -> FlowtimeC.techniqueAlgorithm(startRange, endRange, existingEvent)
            Technique.FiftyTwoSeventeen -> FiftyTwoSeventeen.techniqueAlgorithm(startRange, endRange, existingEvent)
        }
    }

}