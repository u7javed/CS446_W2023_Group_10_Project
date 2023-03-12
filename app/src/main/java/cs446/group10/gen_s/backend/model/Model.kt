package cs446.group10.gen_s.backend.model

import cs446.group10.gen_s.backend.dataClasses.Calendar
import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.dataClasses.Plan
import android.content.Context
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/*"New" things needed to be added to cs446.group10.gen_s.backend.view_model.ViewModel:
- pushing to storage (we think this has to be tied to an android activity)
- pulling from storage (^^)
- creating an event object (given the inputs)
- creating a plan object (given the inputs)

 */

class Model {
    private var views: MutableList<IView> = mutableListOf<IView>()
    private lateinit var context: Context
    private var calendar: Calendar = Calendar("og")
    private var planMap: MutableMap<String, Plan> = mutableMapOf<String, Plan>()
    private var eventMap: MutableMap<String, Event> = mutableMapOf<String, Event>()
    private val FILENAME = "stored_calendar_data.json"

    fun setContext(context: Context) {
        this.context = context
    }

    fun addView(view: IView) {
        this.views.add(view)

    }
    fun getViewLength ():Int{
        return this.views.size
    }

     private fun notifyView() {

         for (view in this.views) {
             view.update()
         }
         pushToStorage(context)
     }

     fun addEvent(event: Event): Boolean {
         // Check if the event conflicts with an existing event
         calendar.events.forEach { curEvent: Event ->
             if ((event.startDate <= curEvent.startDate && event.endDate > curEvent.startDate) ||
                 (event.startDate < curEvent.endDate && event.endDate >= curEvent.endDate) ||
                 (event.startDate <= curEvent.startDate && event.endDate >= curEvent.endDate) ||
                 (event.startDate > curEvent.startDate && event.endDate < curEvent.endDate))
                 return false
         }
         // given an event, add it to the calendar
         calendar.events.add(event)
         // given an event, add to eventMap
         eventMap[event.eventId] = event
         this.notifyView()
         return true
     }

     fun editEvent(eventId: String,
                   name: String,
                   startDate: Long,
                   endDate: Long,
                   notification: Long) {
         var event = getEventById(eventId)
         event?.name = name
         event?.startDate = startDate
         event?.endDate = endDate
         event?.notification = notification

         // given an event, update eventMap
         if (event != null) {
             eventMap[event.eventId] = event
         }
         this.notifyView()
     }

    fun updateEvent(eventId: String, event: Event): Boolean {
        // Check if the event conflicts with an existing event
        if (eventId !in eventMap)
            return false
        calendar.events.forEach { curEvent: Event ->
            if ((curEvent.eventId != eventId) &&
                ((event.startDate <= curEvent.startDate && event.endDate > curEvent.startDate) ||
                (event.startDate < curEvent.endDate && event.endDate >= curEvent.endDate) ||
                (event.startDate <= curEvent.startDate && event.endDate >= curEvent.endDate) ||
                (event.startDate > curEvent.startDate && event.endDate < curEvent.endDate)))
                    return false
        }
        eventMap[eventId]!!.name = event.name
        eventMap[eventId]!!.startDate = event.startDate
        eventMap[eventId]!!.endDate = event.endDate
        eventMap[eventId]!!.notification = event.notification
        this.notifyView()
        return true
    }

     fun removeEvent(eventId: String){
         var event = getEventById(eventId)
         this.calendar.events.remove(event)
         // given an event, add to eventMap
         eventMap.remove(event?.eventId, event)

         this.notifyView()
     }

     fun getEventsData(): MutableList<Event>? {
         // return list of *all* events
         return this.calendar.events
     }

     fun getEventById(eventId: String): Event? {
         // return event given id
         if (eventId in eventMap) {
             return eventMap[eventId]
         }
         return null

     }

     fun addPlan(plan: Plan) {
         // add plan to calendar
         if (plan != null) {
             for (event in plan.events!!) {
                 // QUESTION: why don't we just call addEvent??
                 this.calendar.events.add(event)
                 eventMap[event.eventId] = event
             }
             this.calendar.plans.add(plan)
             // given a plan, update planMap
             planMap[plan.planId] = plan

             this.notifyView()
         }
     }

     fun editPlan(planId: String,
                  name: String,
                  events: MutableList<Event>) {
         var plan = getPlanById(planId)
         plan?.name = name
         plan?.events?.clear()
         plan?.events?.addAll(events)

         // given a plan, update planMap
         if (plan != null) {
             planMap[plan.planId] = plan
         }
         this.notifyView()

     }
     fun removePlan(planId: String){
         var plan = getPlanById(planId)
         if (plan != null) {
             this.calendar.plans.remove(plan)

             for (event in plan!!.events) {
                 this.removeEvent(event.eventId)
             }

             planMap.remove(plan?.planId, plan)

             this.notifyView()
         }

     }
     fun getPlansData(): MutableList<Plan> {
         // return list of plans
         return this.calendar.plans
     }

     fun getPlanById(planId: String): Plan? {
         // get plan from planId
         if (planId in planMap) {
             return planMap[planId]
         }
         return null
     }

     fun getEventsByPlan(planId: String): MutableList<Event>? {
         return this.getPlanById(planId)?.events
     }

    //returns calendar object given calendarId
    fun getCalendar(): Calendar {
        return this.calendar
    }

    // returns JSON string of calendar object given calendarId
    private fun getCalendarAsJSON () : String {
        return Gson().toJson(this.calendar)
    }

    private fun pushToStorage(context: Context){
        try {
            val streamWriter = OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE))
            streamWriter.write(this.getCalendarAsJSON())
            streamWriter.close()
        } catch (e: Exception) {
            println(e)
        }
    }

    private fun pullFromStorage (context: Context){
        var result = ""
        try {
            val stream: FileInputStream = context.openFileInput(FILENAME)
            val streamReader = InputStreamReader(stream)
            val bufferedReader = BufferedReader(streamReader)
            val strBuilder = StringBuilder()
            var retrievedStr = bufferedReader.readLine()
            while (retrievedStr != null) {
                strBuilder.append(retrievedStr)
                retrievedStr = bufferedReader.readLine()
            }
            streamReader.close()
            result = strBuilder.toString()
            val gson = Gson()
            this.calendar = gson.fromJson(result, Calendar::class.java)
        } catch (e: Exception) {
            println("Pull from storage failed! $e")
        }

    }

    fun loadCalendarFromStorage(context: Context) {
        pullFromStorage(context)
        // Update event map
        calendar.events.forEach { event ->
            eventMap[event.eventId] = event
        }
        // Update plan map
        calendar.plans.forEach {plan ->
            planMap[plan.planId] = plan
        }

    }

    fun deleteCalendar() {
        this.calendar = Calendar("og")
        eventMap.clear()
        planMap.clear()
        this.notifyView()
    }

}