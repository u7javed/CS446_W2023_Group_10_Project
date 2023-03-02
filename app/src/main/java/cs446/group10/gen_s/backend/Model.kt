import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class Model {
    private var views: MutableList<View> = mutableListOf<View>()
    private var calendar: Calendar = Calendar("og")
    private var planMap: MutableMap<String, Plan> = mutableMapOf<String, Plan>()
    private var eventMap: MutableMap<String, Event> = mutableMapOf<String, Event>()

    fun addView(view: View) {
        this.views.add(view)

    }
    fun getViewLength ():Int{
        return this.views.size
    }

     fun notifyView() {

         for (view in this.views) {
             view.update()
         }
         // TODO: figure out a way to call pushCalendarToStorage
     }
/*
     fun getCalendarFromStorage(storage: Any): Calendar {
         // TODO: figure out how to get calendar from storage

         // get byte array from internal storage //TODO
         /*val fileName = "Test"

         var fileInputStream: FileInputStream = openFileInput(fileName)
         var inputStreamReader = InputStreamReader(fileInputStream)
         val data = ByteArray(1024)
         stream.read(data)
         stream.close()*/

         var data = byteArrayOf()
         // convert byte array to json string
         var jsonString = Gson().toJson(String(data))
         //convert JSON string to data class (calendar)
         var gson = Gson()
         this.calendar = gson.fromJson(jsonString, Calendar::class.java)

         // TODO: Also create a plan map if not already done from existing data.

         return this.calendar
     }

     fun pushCalendarToStorage(storage: Any) {
         // TODO: figure out how to push calendar to storage
         //  everytime a change happens update storage

         //convert data class (calendar) to JSON string
         var gson = Gson()
         var jsonString = Gson().toJson(this.calendar) // this might not work bc the calendar object is not just made of primitive attributes

         // saving a json string to internal storage //TODO
         // - https://developer.android.com/training/data-storage/app-specific#internal-store-stream
         // - https://www.javatpoint.com/kotlin-android-read-and-write-internal-storage
         /*
         val fileName = "Test"
         val fileOutputStream: FileOutputStream
         try{
             val file = File(context.filesDir, fileName)
         }
         try{

             fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE) //there are errors here -- need to deal with
             fileOutputStream.write(jsonString.toByteArray())
         }
         catch(e: IOException) //TODO - need to implement a try/catch in case there isn't enough space to storage calendar in storage
         {
             e.printStackTrace()
         }*/

     }

*/
     fun addEvent(event: Event) {
         // given an event, add it to the calendar
        this.calendar.events.add(event)
         // given an event, add to eventMap
         eventMap[event.eventId] = event
         this.notifyView()
     }

     fun editEvent(eventId: String,
                   name: String,
                   startDate:LocalDateTime,
                   endDate: LocalDateTime,
                   notification: LocalDateTime) {
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
                 eventMap.put(event.eventId, event)
             }
             this.calendar.plans.add(plan)
             // given a plan, update planMap
             planMap.put(plan.planId, plan)

             this.notifyView()
         }
     }

     fun editPlan(planId: String,
                  name: String,
                  startDate:LocalDateTime,
                  endDate: LocalDateTime,
                  events: MutableList<Event>,
                  preferences: MutableList<Preference>) {
         var plan = getPlanById(planId)
         plan?.name = name
         plan?.startDate = startDate
         plan?.endDate = endDate
         plan?.events = events
         plan?.preferences = preferences

         // given a plan, update planMap
         if (plan != null) {
             planMap[plan.planId] = plan
         }
         this.notifyView()
     }

     fun getPlansData(): MutableList<Plan> {
         // return list of plans
         return this.calendar.plans
     }

     fun getPlanById(planId: String): Plan? {
         // get plan from planId
         if (planId in planMap) {
             return planMap.get(planId)
         }
         return null
     }

     fun getEventsByPlan(planId: String): MutableList<Event>? {
         return this.getPlanById(planId)?.events
     }
}