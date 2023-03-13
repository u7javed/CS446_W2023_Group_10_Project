package cs446.group10.gen_s.backend.dataClasses

data class Plan (
    val planId: String,
    var name: String,
    var start: Long,
    var end: Long,
    val events: MutableList<Event> = mutableListOf(),
    var color: String? = null
)