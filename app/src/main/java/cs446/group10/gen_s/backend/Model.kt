class Model {
    private lateinit var views: ArrayList<Any>
    private lateinit var calendar: Calendar
    private lateinit var planMap: Map<String, Plan>
    private lateinit var eventMap: Map<String, Event>

    fun addView(view: View) {
        this.views.add(view)
    }

    fun notifyView() {
        for (view in this.views) {
            view.update()
        }
        // TODO: figure out a way to call pushCalendarToStorage
    }

    fun getCalendarFromStorage(storage: Any): Calendar {
        // TODO: figure out how to get calendar from storage
        // Also create a plan map if not already done from existing
        // data.
    }

    fun pushCalendarToStorage(storage: Any): Void {
        // TODO: figure out how to push calendar to storage
        //  everytime a change happens update storage
    }


    fun addEvent(event: Event) {
        // given an event, add it to the calendar
        this.calendar.events.add(event)
        this.notifyView()
    }

    fun editEvent(eventId: String,
                  name: String,
                  startDate:String,
                  endDate: String,
                  notification: Date) {
        event = getEventById(eventId)
        event.name = newName
        event.startDate = startDate
        event.endDate = endDate
        event.notification = notification
        this.notifyView()
    }

    fun getEventsData(): MutableList<Event> {
        // return list of *all* events
        return this.calendar.events
    }

    fun getEventById(eventId: String): Event? {
        // return event given id
        if (eventId in eventMap) {
            return eventMap.get(eventId)
        }
        return null

    }

    fun addPlan(plan: Plan): Void {
        // add plan to calendar
        for (event in plan.events) {
            this.calendar.events.add(event)
        }
        this.calendar.plans.add(plan)
        this.notifyView()
    }

    fun editPlan(planId: String,
                 name: String,
                 startDate:String,
                 endDate: String,
                 events: MutableList<Event>,
                 preferences: MutableList<Preferences>) {
        plan = getPlanById(planId)
        plan.name = newName
        plan.startDate = startDate
        plan.endDate = endDate
        plan.events = events
        plan.preferences = preferences
        this.notifyView()
    }

    fun getPlansData(): MutableList<Event> {
        // return list of plans
        return this.calendar.plans
    }

    fun getPlanById(planId: String): Plan {
        // get plan from planId
        if (planId in planMap) {
            return planMap.get(planId)
        }
        return null
    }

    fun getEventsByPlan(planId: String): MutableList<Event> {
        return this.getPlanById(planId).events
    }

}