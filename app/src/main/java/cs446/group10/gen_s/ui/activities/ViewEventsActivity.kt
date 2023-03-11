package cs446.group10.gen_s.ui.activities

import cs446.group10.gen_s.backend.dataClasses.Event
import ViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.model.IView

class ViewEventsActivity : AppCompatActivity(), IView {

    private val _viewModel: ViewModel by lazy {
        ViewModelProvider(this)[ViewModel::class.java]
    }
    private lateinit var _viewEventsLayout: LinearLayout

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your Events"

        _viewEventsLayout = findViewById(R.id.view_events)

        createEventsList(_viewModel.getAllEvents())
    }

    private fun createEventsList(events: List<Event>) {
        events.forEach { event ->
            // eventLayout
            val eventLayout = LinearLayout(this)
            eventLayout.orientation = LinearLayout.HORIZONTAL
            eventLayout.gravity = Gravity.CENTER_VERTICAL
            eventLayout.setPadding(
                dpToPixel(16),
                dpToPixel(6),
                dpToPixel(16),
                dpToPixel(6)
            )

            val eventLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            eventLayoutParams.setMargins(
                dpToPixel(8),
                dpToPixel(12),
                dpToPixel(8),
                0
            )

            eventLayout.layoutParams = eventLayoutParams

            // infoLayout (left)
            val infoLayout = LinearLayout(this)
            infoLayout.orientation = LinearLayout.VERTICAL
            infoLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )

            // eventName (top left)
            val eventNameTextView = TextView(this)
            eventNameTextView.text = event.name
            eventNameTextView.textSize = 16f
            eventNameTextView.setTextColor(Color.BLACK)

            // date (middle left)
            val dateTextView = TextView(this)
            dateTextView.text = "${event.startDate}"
            dateTextView.textSize = 12f
            dateTextView.setTextColor(Color.parseColor("#DD000000"))

            // time (bottom left)
            val timeTextView = TextView(this)
            timeTextView.text = "${event.startDate} - ${event.endDate}"
            timeTextView.textSize = 12f
            timeTextView.setTextColor(Color.parseColor("#DD000000"))

            // infoLayout (left) has 3 children
            infoLayout.addView(eventNameTextView)
            infoLayout.addView(dateTextView)
            infoLayout.addView(timeTextView)

            // chevron image (right)
            val editImageView = ImageButton(this)
            editImageView.setImageResource(R.drawable.editicon)
            editImageView.layoutParams = LinearLayout.LayoutParams(
                dpToPixel(24),
                dpToPixel(24)
            )
            editImageView.setOnClickListener {
                moveToEditEventPage(eventId = event.eventId)
            }
            // eventLayout has 2 children (left, right)
            eventLayout.addView(infoLayout)
            eventLayout.addView(editImageView)

            _viewEventsLayout.addView(eventLayout)
        }
    }

    private fun dpToPixel(_dp: Int) : Int {
        return (_dp * (resources.displayMetrics.density)).toInt()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun moveToEditEventPage(eventId: String): Boolean {
        val editEventIntent = Intent(this, EditEventActivity::class.java)
        editEventIntent.putExtra("eventId", eventId)
        editEventIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(editEventIntent)
        return true
    }

    override fun update() {
        val events: List<Event> = _viewModel.getAllEvents()
        createEventsList(events)
    }
}
