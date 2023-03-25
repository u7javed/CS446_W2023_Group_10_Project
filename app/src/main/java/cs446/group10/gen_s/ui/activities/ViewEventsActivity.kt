package cs446.group10.gen_s.ui.activities

import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.view_model.ViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.model.IView
import cs446.group10.gen_s.ui.adapters.EventListViewAdapter
import java.time.LocalDateTime
import java.time.ZoneOffset

class ViewEventsActivity : AppCompatActivity(), IView {

    private val _viewModel = ViewModel
    private lateinit var _recyclerView: RecyclerView
    private lateinit var _recyclerAdapter: EventListViewAdapter
    private lateinit var noEventText: TextView
    private lateinit var _events: List<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your Events"

        // Update events
        _events = _viewModel.getAllEvents()

        // Get data passed from CalendarActivity
        val intent = intent
        val currentDay = intent.getIntExtra("currentDay", 0)
        val currentMonth = intent.getIntExtra("currentMonth", 0) //0-indexed
        val currentYear = intent.getIntExtra("currentYear", 0)

        // If currentDay is passed from CalendarActivity, render events of a specific day
        if (currentDay != 0) {
            _events = filterTodayEvents(_events, currentDay, currentMonth, currentYear)
        }

        // Register this view to the model
        _viewModel.registerView(this)

        initRecyclerView()
        noEventText = findViewById<TextView>(R.id.noEventText)
        if (_recyclerAdapter.itemCount == 0) {
            _recyclerView.visibility = View.GONE
            noEventText.text = "There are no events yet! Make one to populate this page."
        } else {
            noEventText.visibility = View.GONE
        }
    }

    private fun filterTodayEvents(events: List<Event>, currentDay: Int, currentMonth: Int, currentYear: Int) : List<Event>{
        return events.filter { event ->
            // datetime.month is 1-indexed, currentMonth is 0-indexed
            val startDatetime = LocalDateTime.ofEpochSecond(event.startDate, 0, ZoneOffset.UTC)
            val endDatetime = LocalDateTime.ofEpochSecond(event.endDate, 0, ZoneOffset.UTC)

            val eventStartsToday = (startDatetime.year == currentYear) && (startDatetime.month.value - 1 == currentMonth) && (startDatetime.dayOfMonth == currentDay)

            val currentDatetime = LocalDateTime.of(currentYear, currentMonth + 1, currentDay, 0, 0)
            val eventIncludesToday = (currentDatetime in startDatetime..endDatetime)

            eventStartsToday || eventIncludesToday
        }
    }

    private fun initRecyclerView() {
        _recyclerView = findViewById(R.id.eventsListView)
        _recyclerAdapter = EventListViewAdapter(
            _events,
            onClickListener = ::moveToEditEventScreen
        )
        _recyclerView.layoutManager = LinearLayoutManager(this)
        _recyclerView.adapter = _recyclerAdapter
        _recyclerView.setHasFixedSize(false)
    }

    private fun moveToEditEventScreen(eventId: String) {
        val editEventIntent = Intent(this, EditEventActivity::class.java)
        editEventIntent.putExtra("eventId", eventId)
        editEventIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(editEventIntent)
    }

    override fun update() {
        _recyclerAdapter.updateDataset(_viewModel.getAllEvents())
        if (_recyclerAdapter.itemCount == 0) {
            _recyclerView.visibility = View.GONE
            noEventText.text = "There are no events yet! Make one to populate this page."
        } else {
            noEventText.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
