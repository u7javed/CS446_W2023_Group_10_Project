package cs446.group10.gen_s.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.*
import android.widget.PopupMenu.OnMenuItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cs446.group10.gen_s.R
import java.util.*

class CalendarActivity : AppCompatActivity(), OnMenuItemClickListener {

    private lateinit var actionButton: FloatingActionButton;

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // Get access to floating action button
        actionButton = findViewById(R.id.main_action_button)
        actionButton.setOnClickListener {
            showFabPopup()
        }

        val calendarView = findViewById<LinearLayout>(R.id.calendar_view)

        // Get current month and year
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        // Set calendar to first day of the month
        calendar.set(currentYear, currentMonth, 1)

        // Create a new table layout to hold the calendar
        val tableLayout = TableLayout(this)
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )

        // Create header row for the calendar
        val headerRow = TableRow(this)
        val headerParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headerRow.layoutParams = headerParams

        // Add day labels to header row
        val daysOfWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        for (dayOfWeek in daysOfWeek) {
            val dayLabel = TextView(this)
            dayLabel.text = dayOfWeek
            dayLabel.gravity = Gravity.CENTER
            dayLabel.setPadding(8, 8, 8, 8)
            headerRow.addView(dayLabel)
        }

        // Add header row to table layout
        tableLayout.addView(headerRow)

        // Create a new row for each week in the month
        var currentRow = TableRow(this)
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
            emptyCell.gravity = Gravity.CENTER
            emptyCell.setPadding(8, 8, 8, 8)
            currentRow.addView(emptyCell)
        }

        // Add cells for each day in the month
        var currentDay = 1
        while (currentDay <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            val dayCell = LinearLayout(this)
            dayCell.gravity = Gravity.CENTER
            dayCell.orientation = LinearLayout.VERTICAL
            dayCell.setPadding(8, 8, 8, 8)

            val dayNumberTextView = TextView(this)
            dayNumberTextView.text = currentDay.toString()
            dayNumberTextView.gravity = Gravity.CENTER
            dayCell.addView(dayNumberTextView)

            currentRow.addView(dayCell)

            // Move to the next day
            currentDay++
            calendar.add(Calendar.DAY_OF_MONTH, 1)

            // If reached the end of the week, add the current row to the table layout
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                currentDay > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                tableLayout.addView(currentRow)
                currentRow = TableRow(this)
                currentRow.layoutParams = rowParams
            }
        }

        // Add table layout to calendar view
        calendarView.addView(tableLayout)
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

    data class Event(
        val eventName: String,
        val date: Date?
    )
}