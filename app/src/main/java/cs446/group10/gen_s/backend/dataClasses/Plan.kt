import java.time.LocalDateTime

data class Plan (val planId: String) {
    var name: String? = null
    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null
    var events: MutableList<Event> = mutableListOf()
    var preferences: MutableList<Preference> = mutableListOf()
}