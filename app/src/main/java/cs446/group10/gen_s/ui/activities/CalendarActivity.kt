package cs446.group10.gen_s.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.PopupMenu.OnMenuItemClickListener
import androidx.core.view.contains
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cs446.group10.gen_s.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity(), OnMenuItemClickListener {

    private lateinit var actionButton: FloatingActionButton

    private val calendar = Calendar.getInstance()
    private var currentMonth = calendar.get(Calendar.MONTH)
    private var currentYear = calendar.get(Calendar.YEAR)

    @SuppressLint("SimpleDateFormat")
    private val events : ArrayList<Event> = arrayListOf(
        Event("Event 1", SimpleDateFormat("dd-MM-yyyy").parse("06-02-2023")),
        Event("Event 2", SimpleDateFormat("dd-MM-yyyy").parse("06-02-2023")),
        Event("Event 3", SimpleDateFormat("dd-MM-yyyy").parse("07-02-2023")),
        Event("Event 4", SimpleDateFormat("dd-MM-yyyy").parse("03-03-2023")),
        Event("Event 5", SimpleDateFormat("dd-MM-yyyy").parse("03-03-2023")),
        Event("Event 6", SimpleDateFormat("dd-MM-yyyy").parse("04-03-2023")),
        Event("Event 7", SimpleDateFormat("dd-MM-yyyy").parse("05-03-2023")),
        Event("Event 8", SimpleDateFormat("dd-MM-yyyy").parse("06-03-2023")),
        Event("Event 9", SimpleDateFormat("dd-MM-yyyy").parse("06-03-2023")),
        Event("Event 10", SimpleDateFormat("dd-MM-yyyy").parse("07-03-2023")),
        Event("Event 11", SimpleDateFormat("dd-MM-yyyy").parse("06-04-2023")),
        Event("Event 12", SimpleDateFormat("dd-MM-yyyy").parse("06-04-2023")),
        Event("Event 13", SimpleDateFormat("dd-MM-yyyy").parse("07-04-2023")),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        supportActionBar?.title = "GenS"

        // Get access to floating action button
        actionButton = findViewById(R.id.main_action_button)
        actionButton.setOnClickListener {
            showFabPopup()
        }

        renderCalender()
    }

    fun onNextMonthHandler(view: View) {
        if (currentMonth == 11) {
            currentMonth = 0
            currentYear++
        } else {
            currentMonth++
        }

        renderCalender()
    }

    fun onPreviousMonthHandler(view: View) {
        if (currentMonth == 0) {
            currentMonth = 11
            currentYear--
        } else {
            currentMonth--
        }

        renderCalender()
    }

    @SuppressLint("SetTextI18n")
    private fun renderCalender() {
        val yearMonthTextView = findViewById<TextView>(R.id.calendar_year_month)
        val tableLayout = findViewById<LinearLayout>(R.id.calendar_table)

        // Remove all views in tableLayout
        tableLayout.removeAllViews()

        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT,
        )

        val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        yearMonthTextView.text = "${months[currentMonth]} $currentYear"

        // Set calendar to first day of the month
        calendar.set(currentYear, currentMonth, 1)

        // Create header row for the calendar
        val headerRow = TableRow(this)
        headerRow.weightSum = 7f
        val headerParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headerRow.layoutParams = headerParams

        val cellParams = TableRow.LayoutParams(
            0,
            dpToPixel(80),
            1f
        )
        cellParams.setMargins(
            dpToPixel(3),
            dpToPixel(3),
            dpToPixel(3),
            dpToPixel(3)
        )

        // Add day labels to header row
        val daysOfWeek = arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
        for (dayOfWeek in daysOfWeek) {
            val dayLabel = TextView(this)
            dayLabel.text = dayOfWeek
            dayLabel.gravity = Gravity.CENTER
            dayLabel.textSize = 12f
            dayLabel.setTextColor((Color.parseColor("#FF000000")))

            val dayLabelParams = TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f
            )
            dayLabelParams.setMargins(
                dpToPixel(3),
                dpToPixel(16),
                dpToPixel(3),
                dpToPixel(16)
            )
            dayLabel.layoutParams = dayLabelParams

            headerRow.addView(dayLabel)
        }

        // Add header row to table layout
        tableLayout.addView(headerRow)

        // Create a new row for each week in the month
        var currentRow = TableRow(this)
        currentRow.weightSum = 7f
        val rowParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        currentRow.layoutParams = rowParams

        // Add empty cells for days before the first day of the month
        val emptyDays = calendar.get(Calendar.DAY_OF_WEEK) - 1
        for (i in 1..emptyDays) {
            val emptyCell = TextView(this)
            emptyCell.text = ""
            emptyCell.layoutParams = cellParams
            currentRow.addView(emptyCell)
        }

        // Add cells for each day in the month
        var currentDay = 1
        while (currentDay <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            val dayCell = LinearLayout(this)
            dayCell.orientation = LinearLayout.VERTICAL
            dayCell.layoutParams = cellParams

            val dayNumberTextView = TextView(this)
            dayNumberTextView.text = currentDay.toString()
            dayNumberTextView.gravity = Gravity.CENTER
            dayNumberTextView.textSize = 18f
            dayNumberTextView.setTextColor((Color.parseColor("#FF000000")))

            dayCell.addView(dayNumberTextView)

            // Add events to the day cell
            val eventsForDay = events.filter { event ->
                val eventCalendar = Calendar.getInstance()
                eventCalendar.time = event.date!!
                eventCalendar.get(Calendar.DAY_OF_MONTH) == currentDay &&
                        eventCalendar.get(Calendar.MONTH) == currentMonth &&
                        eventCalendar.get(Calendar.YEAR) == currentYear
            }

            if (eventsForDay.isNotEmpty()) {
                // Create a new linear layout to hold the events for the day
                val eventsLayout = LinearLayout(this)
                eventsLayout.orientation = LinearLayout.VERTICAL

                // Add each event to the linear layout
                for (event in eventsForDay) {
                    val eventTextView = TextView(this)
                    eventTextView.text = event.eventName
                    eventTextView.gravity = Gravity.CENTER
                    eventTextView.textSize = 9f
                    eventTextView.setTextColor((Color.parseColor("#FFFFFFFF")))
                    eventTextView.setBackgroundColor((Color.parseColor("#FF6169AE")))

                    val eventTextViewParams = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    eventTextViewParams.setMargins(0, 0, 0, dpToPixel(2))
                    eventTextView.layoutParams = eventTextViewParams

                    eventsLayout.addView(eventTextView)
                }

                // Add the linear layout to the day cell
                dayCell.addView(eventsLayout)
            }

            currentRow.addView(dayCell)

            // Move to the next day
            currentDay++
            calendar.add(Calendar.DAY_OF_MONTH, 1)

            if (currentDay > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                // Add empty cells for days after the last day of the month
                val tailingEmptyDays = 7 - (calendar.get(Calendar.DAY_OF_WEEK) - 1)

                if (tailingEmptyDays < 7) {
                    for (i in 1..tailingEmptyDays) {
                        val emptyCell = TextView(this)
                        emptyCell.text = ""
                        emptyCell.layoutParams = cellParams
                        currentRow.addView(emptyCell)
                    }
                    tableLayout.addView(currentRow)
                }
            }

            // If reached the end of the week, add the current row to the table layout
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                tableLayout.addView(currentRow)
                currentRow = TableRow(this)
                currentRow.layoutParams = rowParams
            }
        }
    }

    private fun dpToPixel(_dp: Int) : Int {
        return (_dp * (resources.displayMetrics.density)).toInt()
    }

    /** This function creates a pop up menu from the FAB button **/
    private fun showFabPopup() {
        // Create a popup menu that activates with the action button
        val popupMenu = PopupMenu(this, actionButton)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.fab_menu)
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fab_add_event -> moveToAddEventScreen()
            R.id.fab_auto_generate -> moveToAutoGenerateScreen()
            R.id.fab_generate_plan -> moveToGeneratePlanScreen()
            R.id.fab_view_events -> moveToViewEventsScreen()
            R.id.fab_import_calendar -> moveToImportCalendarScreen()
            R.id.fab_view_plans -> moveToViewPlansScreen()
            else -> false
        }
    }

    private fun moveToAddEventScreen(): Boolean {
        val addEventIntent = Intent(this, AddEventActivity::class.java)
        startActivity(addEventIntent)
        return true
    }

    private fun moveToAutoGenerateScreen(): Boolean {
        val autoGenerateIntent = Intent(this, AutoGenerateActivity::class.java)
        startActivity(autoGenerateIntent)
        return true
    }

    private fun moveToGeneratePlanScreen(): Boolean {
        val generatePlanIntent = Intent(this, GeneratePlanActivity::class.java)
        startActivity(generatePlanIntent)
        return true
    }

    private fun moveToViewEventsScreen(): Boolean {
        val viewEventsIntent = Intent(this, ViewEventsActivity::class.java)
        startActivity(viewEventsIntent)
        return true
    }

    private fun moveToImportCalendarScreen(): Boolean {
        val importCalendarIntent = Intent(this, ImportCalendarActivity::class.java)
        startActivity(importCalendarIntent)
        return true
    }
    
    private fun moveToViewPlansScreen(): Boolean {
        val viewPlansIntent = Intent(this, ViewPlansActivity::class.java)
        startActivity(viewPlansIntent)
        return true
    }

    data class Event(
        val eventName: String,
        val date: Date?
    )
}