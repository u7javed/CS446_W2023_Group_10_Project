package cs446.group10.gen_s.backend.dataClasses

data class Event(
    val eventId: String,
    var name: String,
    var startDate: Long,
    var endDate: Long,
    var notification: Long? = null,
    var planId: String? = null,
    var color: String = "#4472AF"
)
