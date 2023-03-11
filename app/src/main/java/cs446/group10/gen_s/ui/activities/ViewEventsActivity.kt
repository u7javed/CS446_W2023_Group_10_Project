package cs446.group10.gen_s.ui.activities

import ViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import cs446.group10.gen_s.R

class ViewEventsActivity : AppCompatActivity() {

    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this)[ViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your Events"

        val viewEventsLayout = findViewById<LinearLayout>(R.id.view_events)

        val events : ArrayList<Event> = arrayListOf<Event>(
            Event("Event 1", "Jan 3", "8am", "10am"),
            Event("Event 2", "Jan 4", "11am", "12pm"),
            Event("Event 3", "Jan 5", "1pm", "3pm"),
            Event("Event 4", "Jan 6", "4pm", "6pm"),
            Event("Event 5", "Jan 7", "7pm", "9pm")
        )

        for (event in events) {
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
            eventNameTextView.text = event.eventName
            eventNameTextView.textSize = 16f
            eventNameTextView.setTextColor(Color.BLACK)

            // date (middle left)
            val dateTextView = TextView(this)
            dateTextView.text = event.date
            dateTextView.textSize = 12f
            dateTextView.setTextColor(Color.parseColor("#DD000000"))

            // time (bottom left)
            val timeTextView = TextView(this)
            timeTextView.text = "${event.startTime} - ${event.endTime}"
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
            editImageView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    moveToEditEventPage(event.eventName, event.date, event.startTime, event.endTime)
                }
            })
            // eventLayout has 2 children (left, right)
            eventLayout.addView(infoLayout)
            eventLayout.addView(editImageView)

            viewEventsLayout.addView(eventLayout)
        }
    }

    private fun dpToPixel(_dp: Int) : Int {
        return (_dp * (resources.displayMetrics.density)).toInt()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    data class Event(
        val eventName: String,
        val date: String,
        val startTime: String,
        val endTime: String
    )

    private fun moveToEditEventPage(eventName: String, date: String, startTime: String, endTime: String): Boolean {
        val editEventIntent = Intent(this, EditEventActivity::class.java)
        editEventIntent.putExtra("eventName", eventName)
        editEventIntent.putExtra("date", date)
        editEventIntent.putExtra("startTime", startTime)
        editEventIntent.putExtra("endTime", endTime)
        editEventIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(editEventIntent)
        return true
    }
}
