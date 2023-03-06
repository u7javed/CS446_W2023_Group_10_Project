package cs446.group10.gen_s.backend.model

import Event

class EventViewMock(model: Model) : View(model) {

    init {
        model.addView(this)
    }

    override fun update() {
        val d: MutableList<Event>? = model.getEventsData()
        // update UI given events data
    }

}