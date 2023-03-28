package cs446.group10.gen_s

import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.model.Model
import cs446.group10.gen_s.backend.dataClasses.Plan
import cs446.group10.gen_s.backend.dataClasses.Preference
import cs446.group10.gen_s.backend.model.ViewMock
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset


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
        var test_view = ViewMock(model)
        model.addView(test_view)
        assertEquals(1, model.getViewLength())
        //assertEquals("updated view", model.notifyView())
    }
    @Test
    // addEvent; getEventsById; editEvent; getEventsData; removeEvent
    fun event_test(){
        var event = Event(
            "test",
            "Test",
            LocalDateTime.parse("2018-12-14T09:55:00").atZone(ZoneId.systemDefault()).toEpochSecond(),
            LocalDateTime.parse("2019-12-14T09:55:00").atZone(ZoneId.systemDefault()).toEpochSecond(),
            LocalDateTime.parse("2019-10-14T09:55:00").atZone(ZoneId.systemDefault()).toEpochSecond()
        )

        model.addEvent(event)
        assertEquals(event, model.getEventById("test"))

        var mod_event = Event(
            "test",
            "Test",
            LocalDateTime.parse("2018-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2022-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2019-10-14T09:55:00").toEpochSecond(ZoneOffset.UTC)
        )

        model.editEvent(
            "test",
            "Test",
            LocalDateTime.parse("2018-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2022-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2019-10-14T09:55:00").toEpochSecond(ZoneOffset.UTC)
        )
        assertEquals(event, mod_event)

        assertEquals(mutableListOf(event), model.getEventsData())

        model.removeEvent("test")
        assertEquals(mutableListOf<Event>(), model.getEventsData())
    }
    @Test
    // addPlan; getPlanById; editPlan; getEventsByPlan; removePlan
    fun plan_test(){
        var event = Event(
            "test1",
            "Test",
            LocalDateTime.parse("2018-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2019-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2019-10-14T09:55:00").toEpochSecond(ZoneOffset.UTC)
        )

        var mod_event = Event(
            "test2",
            "Test",
            LocalDateTime.parse("2018-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2022-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2019-10-14T09:55:00").toEpochSecond(ZoneOffset.UTC)
        )

        var pref = Preference(
            "pref1",
            "Test",
            LocalDateTime.parse("2018-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.parse("2022-12-14T09:55:00").toEpochSecond(ZoneOffset.UTC),
            1
        )

        var plan = Plan(
            "plan1",
            "cs446.group10.gen_s.backend.dataClasses.Plan Test",
            mutableListOf(event, mod_event)
        )

        model.addPlan(plan)
        assertEquals(plan, model.getPlanById("plan1"))

        plan.events.clear()
        plan.events.add(event)
        model.editPlan(
            "plan1",
            "cs446.group10.gen_s.backend.dataClasses.Plan Test",
            mutableListOf(event))
        assertEquals(plan, model.getPlanById("plan1"))

        assertEquals(mutableListOf(event), model.getEventsByPlan("plan1"))
        assertEquals(mutableListOf(plan), model.getPlansData())

        model.removePlan("plan1")
        assertEquals(mutableListOf<Plan>(), model.getPlansData())
    }
    /*
    @Test
    fun storage_test(){
        var event = cs446.group10.gen_s.backend.dataClasses.Event("test")
        event.name = "Test"
        event.startDate = LocalDateTime.parse("2018-12-14T09:55:00")
        event.endDate =LocalDateTime.parse("2019-12-14T09:55:00")
        event.notification = LocalDateTime.parse("2019-10-14T09:55:00")

        var mod_event = cs446.group10.gen_s.backend.dataClasses.Event("test2")
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


