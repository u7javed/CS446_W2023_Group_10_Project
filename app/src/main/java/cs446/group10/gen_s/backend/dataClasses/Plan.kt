package cs446.group10.gen_s.backend.dataClasses

data class Plan (
    val planId: String,
    var name: String,
    val events: MutableList<Event> = mutableListOf()
)