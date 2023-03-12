package cs446.group10.gen_s.backend.dataClasses

data class Calendar (val calendarId: String) {
    var plans: MutableList<Plan> = mutableListOf()
    var events: MutableList<Event> = mutableListOf() // StandAlones
}