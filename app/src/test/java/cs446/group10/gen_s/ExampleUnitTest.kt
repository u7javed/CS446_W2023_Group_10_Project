package cs446.group10.gen_s

import Event
import Model
import Plan
import Preference
import View
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class ModelUnitTests {
    var model = Model()
    @Test
    fun addView_test(){
        var test_view = View(model)
        model.addView(test_view)
        assertEquals(1, model.getViewLength())
        //assertEquals("updated view", model.notifyView())
    }
    @Test
    // addEvent; getEventsById; editEvent; getEventsData; removeEvent
    fun event_test(){
        var event = Event("test")
        event.name = "Test"
        event.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        event.endDate =LocalDateTime.parse("2019-12-14T09:55:00")
        event.notification = LocalDateTime.parse("2019-10-14T09:55:00")

        model.addEvent(event)
        assertEquals(event, model.getEventById("test"))

        var mod_event = Event("test")
        mod_event.name = "Test"
        mod_event.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        mod_event.endDate =LocalDateTime.parse("2022-12-14T09:55:00")
        mod_event.notification = LocalDateTime.parse("2019-10-14T09:55:00")

        model.editEvent("test", "Test",  LocalDateTime.parse("2018-12-14T09:55:00"),LocalDateTime.parse("2022-12-14T09:55:00"), LocalDateTime.parse("2019-10-14T09:55:00"))
        assertEquals(event, mod_event)

        assertEquals(mutableListOf(event), model.getEventsData())

        model.removeEvent("test")
        assertEquals(mutableListOf<Event>(), model.getEventsData())
    }
    @Test
    // addPlan; getPlanById; editPlan; getEventsByPlan; removePlan
    fun plan_test(){
        var event = Event("test1")
        event.name = "Test"
        event.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        event.endDate =LocalDateTime.parse("2019-12-14T09:55:00")
        event.notification = LocalDateTime.parse("2019-10-14T09:55:00")

        var mod_event = Event("test2")
        mod_event.name = "Test"
        mod_event.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        mod_event.endDate =LocalDateTime.parse("2022-12-14T09:55:00")
        mod_event.notification = LocalDateTime.parse("2019-10-14T09:55:00")

        var pref = Preference("pref1")
        pref.name = "Test"
        pref.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        pref.endDate =LocalDateTime.parse("2022-12-14T09:55:00")
        pref.priority = 1

        var plan = Plan("plan1")
        plan.name = "Plan Test"
        plan.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        plan.endDate = LocalDateTime.parse("2025-12-14T09:55:00")
        plan.events = mutableListOf<Event>(event, mod_event)
        plan.preferences = mutableListOf<Preference>(pref)

        model.addPlan(plan)
        assertEquals(plan, model.getPlanById("plan1"))

        plan.events = mutableListOf<Event>(event)
        model.editPlan("plan1", "Plan Test",  LocalDateTime.parse("2018-12-14T09:55:00"),LocalDateTime.parse("2025-12-14T09:55:00"), mutableListOf<Event>(event), mutableListOf<Preference>(pref))
        assertEquals(plan, model.getPlanById("plan1"))

        assertEquals(mutableListOf(event), model.getEventsByPlan("plan1"))
        assertEquals(mutableListOf(plan), model.getPlansData())

        model.removePlan("plan1")
        assertEquals(mutableListOf<Plan>(), model.getPlansData())
    }
    /*
    @Test
    fun storage_test(){
        var event = Event("test")
        event.name = "Test"
        event.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        event.endDate =LocalDateTime.parse("2019-12-14T09:55:00")
        event.notification = LocalDateTime.parse("2019-10-14T09:55:00")

        var mod_event = Event("test2")
        mod_event.name = "Test"
        mod_event.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        mod_event.endDate =LocalDateTime.parse("2022-12-14T09:55:00")
        mod_event.notification = LocalDateTime.parse("2019-10-14T09:55:00")

        model.addEvent(event)
        model.addEvent(mod_event)

        //model.pushCalendarToStorage()
        //model.getCalendarFromStorage()
    }

     */
}


