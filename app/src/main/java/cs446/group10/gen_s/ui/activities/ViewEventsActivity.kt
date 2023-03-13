package cs446.group10.gen_s.ui.activities

import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.view_model.ViewModel
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.model.IView
import cs446.group10.gen_s.ui.adapters.EventListViewAdapter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class ViewEventsActivity : AppCompatActivity(), IView {

    private val _viewModel = ViewModel
    private lateinit var _recyclerView: RecyclerView
    private lateinit var _recyclerAdapter: EventListViewAdapter
    private lateinit var _events: List<Event>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your Events"

        // Update events
        _events = _viewModel.getAllEvents()

        // Register this view to the model
        _viewModel.registerView(this)

        initRecyclerView()
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
        _events = _viewModel.getAllEvents()
        _recyclerAdapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
