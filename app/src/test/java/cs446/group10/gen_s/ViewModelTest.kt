import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.dataClasses.Plan
import cs446.group10.gen_s.backend.dataClasses.Preference
import org.junit.Test
import org.junit.Assert.*

class ViewModelTest {

    var viewModel = ViewModel()

    @Test
    fun testAllocationAlgorithm() {
        // Setup mock to return the expected array
        val preferences: List<Preference> = listOf(
            // startRange: 8:00am, endRange: 12:00pm, duration: 1 hr
            Preference("1", "morning_run", 1677916800, 1677931200, 3600),
            // startRange: 8:00am, endRange: 8:00pm, duration: 4 hr
            Preference("2", "evening_session", 1677916800, 1677960000, 14400),
            // startRange: 12:00pm, endRange: 8:00pm, duration: 0.5 hr
            Preference("3", "lunch_break", 1677931200, 1677960000, 1800),
        )
        val startRange = 1677916800L // 8:00 am
        val endRange = 1677960000L // 8:00 pm

        val plan: Plan? = viewModel.generatePlan("planTest", preferences, startRange, endRange)
        assertNotNull(plan)

        val events: List<Event> = plan!!.events

        assertEquals(events[0].name, "morning_run")
        assertEquals(events[1].name, "evening_session")
        assertEquals(events[2].name, "lunch_break")

        assertEquals(events[0].startDate, 1677920400L)
        assertEquals(events[0].endDate, 1677924000L)
        assertEquals(events[1].startDate, 1677942000L)
        assertEquals(events[1].endDate, 1677956400L)
        assertEquals(events[2].startDate, 1677938400L)
        assertEquals(events[2].endDate, 1677940200L)
    }
}