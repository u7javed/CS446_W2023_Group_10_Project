package cs446.group10.gen_s.backend.model

import Event

class ViewMock(var model: Model) : IView {

    init {

    }

    override fun update() {
        //val plans: Any = model.getPlansData()
        val events: MutableList<Event>? = model.getEventsData()
        // update UI given calendar data (plans, events)

    }

}

