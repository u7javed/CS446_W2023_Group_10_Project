data class Calendar (val calendarId: String) {
    var plans: MutableList<Plan>? = null
    var events: MutableList<Event>? = null
}