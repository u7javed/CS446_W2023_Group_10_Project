
class View(var model: Model) {

    init {

    }

    fun update() {
        //val plans: Any = model.getPlansData()
        val events: MutableList<Event>? = model.getEventsData()
        // update UI given calendar data (plans, events)

    }

}

