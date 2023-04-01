package cs446.group10.gen_s.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cs446.group10.gen_s.*
import cs446.group10.gen_s.backend.dataClasses.Plan
import cs446.group10.gen_s.backend.model.IView
import cs446.group10.gen_s.backend.view_model.ViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset

class GeneratedPlanActivity : AppCompatActivity(), IView {
    private lateinit var generatedEventsLayout: LinearLayout;
    private lateinit var planId: String;
    private fun redirectToHome() {
        val calendarIntent = Intent(this, CalendarActivity::class.java)
        calendarIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(calendarIntent)
    }

    private fun showEvents(plan: Plan?) {
        generatedEventsLayout.removeAllViews();
        val nonMappedGeneratedEvents = plan!!.events;

        var generatedEvents : ArrayList<Event> = arrayListOf();
        for (event in nonMappedGeneratedEvents) {
            val startDateTime = LocalDateTime.ofEpochSecond(event.startDate, 0, ZoneOffset.UTC)
            val startDate = DateVal(
                startDateTime.year,
                startDateTime.monthValue - 1,
                startDateTime.dayOfMonth,
            );
            val startTime = TimeVal(
                startDateTime.hour,
                startDateTime.minute,
            )
            val endDateTime = LocalDateTime.ofEpochSecond(event.endDate, 0, ZoneOffset.UTC)
            val endDate = DateVal(
                endDateTime.year,
                endDateTime.monthValue - 1,
                endDateTime.dayOfMonth,
            );
            val endTime = TimeVal(
                endDateTime.hour,
                endDateTime.minute,
            )

            var dateStr = DatePickerFragment.convertDateToString(startDate);
            if (startDate.year != endDate.year ||
                startDate.month != endDate.month ||
                startDate.day != endDate.day) {
                dateStr += " - " + DatePickerFragment.convertDateToString(endDate);
            }

            var startTimeStr = TimePickerFragment.convertTimeToString(startTime);
            var endTimeStr = TimePickerFragment.convertTimeToString(endTime);

            generatedEvents.add(
                Event(
                    event.eventId,
                    event.name,
                    dateStr,
                    startTimeStr,
                    endTimeStr,
                )
            )
        }

        for (event in generatedEvents) {
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
            val chevronImageView = ImageView(this)
            chevronImageView.setImageResource(R.drawable.chevron)
            chevronImageView.layoutParams = LinearLayout.LayoutParams(
                dpToPixel(24),
                dpToPixel(24)
            )

            fun moveToEditEventScreen(eventId: String) {
                val editEventIntent = Intent(this, EditEventActivity::class.java)
                editEventIntent.putExtra("eventId", eventId)
                editEventIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(editEventIntent)
            }
            eventLayout.setOnClickListener {
                moveToEditEventScreen(event.eventId);
            }

            // eventLayout has 2 children (left, right)
            eventLayout.addView(infoLayout)
            eventLayout.addView(chevronImageView)

            generatedEventsLayout.addView(eventLayout)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generated_plan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your New cs446.group10.gen_s.backend.dataClasses.Plan"

        generatedEventsLayout = findViewById(R.id.generated_events)
        ViewModel.registerView(this)

        val extras = intent.extras

        if (extras == null) {
            redirectToHome();
            return;
        }
        planId = extras!!.getString("planId")!!
        if (planId == null) {
            redirectToHome();
            return;
        }

        val plan: Plan? = ViewModel.getPlanById(planId);
        if (plan == null) {
            redirectToHome();
            return;
        }

        showEvents(plan);
    }

    private fun dpToPixel(_dp: Int) : Int {
        return (_dp * (resources.displayMetrics.density)).toInt()
    }

    fun cancelBtnClickHandler(view: View) {
        ViewModel.removePlanById(planId);
        redirectToHome();
//        Toast.makeText(this, "Cancel Button Clicked", Toast.LENGTH_SHORT).show()
    }

    fun confirmBtnClickHandler(view: View) {
        redirectToHome();
//        Toast.makeText(this, "Confirm Button Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun update() {
        val plan: Plan? = ViewModel.getPlanById(planId);
        if (plan == null) {
            redirectToHome();
            return;
        }

        showEvents(plan);
    }

    data class Event(
        val eventId: String,
        val eventName: String,
        val date: String,
        val startTime: String,
        val endTime: String
    )
}