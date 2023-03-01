import java.util.*

data class Plan (val planId: String) {
    var name: String? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var events: MutableList<Event>? = null
    var preferences: MutableList<Preference>? = null
}