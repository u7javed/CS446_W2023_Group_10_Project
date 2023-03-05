data class Event(
    val eventId: String,
    var name: String,
    var startDate: Long,
    var endDate: Long,
    var notification: Long? = null
)
