package cs446.group10.gen_s.backend.model

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import cs446.group10.gen_s.backend.dataClasses.Calendar
import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.dataClasses.Plan
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import cs446.group10.gen_s.backend.dataClasses.IntentSaver
import cs446.group10.gen_s.backend.notifications.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.max
import kotlin.math.min

/*"New" things needed to be added to cs446.group10.gen_s.backend.view_model.ViewModel:
- pushing to storage (we think this has to be tied to an android activity)
- pulling from storage (^^)
- creating an event object (given the inputs)
- creating a plan object (given the inputs)

 */

class Model {
    private var views: MutableList<IView> = mutableListOf<IView>()
    private lateinit var context: Context
    private lateinit var alarmManager: AlarmManager
    private var calendar: Calendar = Calendar("og")
    private var planMap: MutableMap<String, Plan> = mutableMapOf()
    private var eventMap: MutableMap<String, Event> = mutableMapOf()
    private var notificationMap: MutableMap<String, PendingIntent> = mutableMapOf()
    private val FILENAME = "stored_calendar_data.json"


    fun setContext(context: Context) {
        this.context = context
    }

    fun getContext() : Context {
        return this.context
    }

    fun addView(view: IView) {
        this.views.add(view)

    }

    fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "This notification channel notifies of any upcoming events and their corresponding times"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun scheduleNotification(eventId: String, notification: Long, title: String, message: String) {
        val intent = Intent(context.applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (eventId in notificationMap)
            removeNotification(eventId)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            notification * 1000,
            pendingIntent
        )

        // Map eventId to this pending intent
        notificationMap[eventId] = pendingIntent
        calendar.notifications[eventId] = IntentSaver(title, message)
        pushToStorage(context)
    }

    private fun removeNotification(eventId: String) {
        if (eventId in notificationMap) {
            alarmManager.cancel(notificationMap[eventId])
            notificationMap.remove(eventId)
        }
        if (eventId in calendar.notifications) {
            calendar.notifications.remove(eventId)
            pushToStorage(context)
        }
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

     fun addEvent(event: Event, planId: String? = null): Boolean {
         // Check if the event conflicts with an existing event
         calendar.events.forEach { curEvent: Event ->
             if ((event.startDate <= curEvent.startDate && event.endDate > curEvent.startDate) ||
                 (event.startDate < curEvent.endDate && event.endDate >= curEvent.endDate) ||
                 (event.startDate <= curEvent.startDate && event.endDate >= curEvent.endDate) ||
                 (event.startDate > curEvent.startDate && event.endDate < curEvent.endDate))
                 return false
         }
         if (planId != null && planId in planMap) {
             if (planMap[planId]?.color != null)
                 event.color = planMap[planId]?.color!!
             event.planId = planId
             planMap[planId]?.events?.add(event)
             if (event.startDate < planMap[planId]?.start!!)
                 planMap[planId]?.start = event.startDate
             if (event.endDate > planMap[planId]?.end!!)
                 planMap[planId]?.end = event.endDate
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

    fun deleteEvent(eventId: String): Boolean {
        if (eventId !in eventMap) {
            println("Event deletion failed! Event Id not found in calendar.")
            return false
        }
        return try {
            this.calendar.events.remove(eventMap[eventId])

            // Remove event from plan if in one
            if (eventMap[eventId]?.planId != null) {
                val plan = getPlanById(eventMap[eventId]?.planId!!)
                plan?.events?.remove(eventMap[eventId])
                if (plan?.events?.isEmpty() == true) {
                    this.removePlan(eventMap[eventId]?.planId!!)
                } else {
                    plan?.events?.forEach { event: Event ->
                        plan.start = min(plan.start, event.startDate)
                        plan.end = max(plan.end, event.endDate)
                    }
                }
            }

            eventMap.remove(eventId)
            // Remove notification
            removeNotification(eventId)
            this.notifyView()
            true
        } catch (e: Exception) {
            println("Event deletion failed! $e")
            false
        }
    }

    fun updateEvent(eventId: String, event: Event, planId: String? = null): Boolean {
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
        if (planId != null && planId in planMap && eventMap[eventId]!! !in planMap[planId]!!.events) {
            for (event in planMap[planId]!!.events) {
                if (event.eventId == eventId) {
                    planMap[planId]?.events?.remove(event)
                    break;
                }
            }

            if (planMap[planId]?.color != null)
                eventMap[eventId]!!.color = planMap[planId]?.color!!
            eventMap[eventId]!!.planId = planId
            planMap[planId]?.events?.add(eventMap[eventId]!!)
            if (eventMap[eventId]!!.startDate < planMap[planId]?.start!!)
                planMap[planId]?.start = eventMap[eventId]!!.startDate
            if (eventMap[eventId]!!.endDate > planMap[planId]?.end!!)
                planMap[planId]?.end = eventMap[eventId]!!.endDate
        }

        this.notifyView()
        return true
    }

     fun removeEvent(eventId: String){
         var event = getEventById(eventId)
         this.calendar.events.remove(event)
         // given an event, add to eventMap
         eventMap.remove(event?.eventId, event)
         // Delete notification
         removeNotification(eventId)

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
         for (event in plan.events) {
             // QUESTION: why don't we just call addEvent??
             this.calendar.events.add(event)
             eventMap[event.eventId] = event
         }
         this.calendar.plans.add(plan)
         // given a plan, update planMap
         planMap[plan.planId] = plan

         this.notifyView()
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

             for (event in plan.events) {
                 this.removeEvent(event.eventId)
             }

             planMap.remove(plan.planId, plan)

             this.notifyView()
         }

     }
     fun getPlansData(): MutableList<Plan> {
         // return list of plans
         return this.calendar.plans
     }

    fun getPlanName(planId: String): String? {
        return if (planId !in planMap) {
            null
        } else {
            planMap[planId]?.name
        }
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
        // Update notifications map
        convertIntentSaversToPendingIntents()

    }

    private fun convertIntentSaversToPendingIntents() {
        calendar.notifications.forEach { (eventId, intentSaver) ->
            val intent = Intent(context.applicationContext, Notification::class.java)
            intent.putExtra(titleExtra, intentSaver.title)
            intent.putExtra(messageExtra, intentSaver.message)
            val pendingIntent = PendingIntent.getBroadcast(
                context.applicationContext,
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            notificationMap[eventId] = pendingIntent
        }
    }

    fun updatePlanName(planId: String, newName: String) {
        var plan = getPlanById(planId)
        if (plan != null) {
            plan.name = newName
            this.notifyView()
        }
    }

    fun updatePlanColor(planId: String, newColor: String) {
        var plan = getPlanById(planId)
        if (plan != null) {
            plan.color = newColor
            plan.events.forEach { event: Event ->
                event.color = newColor
            }
            this.notifyView()
        }
    }

    fun deleteCalendar() {
        this.calendar = Calendar("og")
        eventMap.clear()
        planMap.clear()
        val keys = notificationMap.keys.toList()
        keys.forEach { eventId ->
            removeNotification(eventId)
        }
        notificationMap.clear()
        this.notifyView()
    }

}