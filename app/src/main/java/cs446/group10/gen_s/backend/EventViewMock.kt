class EventViewMock(model: Model) {

    init {
        model.addView(this)
    }

    fun update() {
        val d: Any = model.getEventsData()
        // update UI given events data
    }

}