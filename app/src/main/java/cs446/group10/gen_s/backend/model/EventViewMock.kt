package cs446.group10.gen_s.backend.model

import cs446.group10.gen_s.backend.dataClasses.Event

class EventViewMock(private val model: Model) : IView {

    init {
        model.addView(this)
    }

    override fun update() {
        val d: MutableList<Event>? = model.getEventsData()
        // update UI given events data
    }

}