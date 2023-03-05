package cs446.group10.gen_s.backend.model

import Event

open class View(var model: Model) {

    init {

    }

    open fun update() {
        //val plans: Any = model.getPlansData()
        val events: MutableList<Event>? = model.getEventsData()
        // update UI given calendar data (plans, events)

    }

}

