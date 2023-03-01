class CalendarViewMock(model: Model) {

    init {
        model.addView(this)
    }

    fun update() {
        val plans: Any = model.getPlansData()
        val events: Any = model.getEventsData()
        // update UI given calendar data (plans, events)
    }

}