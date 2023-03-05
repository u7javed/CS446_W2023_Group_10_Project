data class Calendar (val calendarId: String) {
    var plans: MutableList<Plan> = mutableListOf()
    var events: MutableList<Event> = mutableListOf()
}