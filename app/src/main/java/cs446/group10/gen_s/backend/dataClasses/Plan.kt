data class Plan (
    val planId: String,
    var name: String,
    val events: MutableList<Event> = mutableListOf()
)