package cs446.group10.gen_s.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.PopupMenu.OnMenuItemClickListener
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.model.IView
import cs446.group10.gen_s.backend.view_model.ViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class CalendarActivity : AppCompatActivity(), OnMenuItemClickListener, IView {

    private lateinit var actionButton: FloatingActionButton

    private val calendar = Calendar.getInstance()
    private var currentMonth = calendar.get(Calendar.MONTH) // 0-indexed
    private var currentYear = calendar.get(Calendar.YEAR)
    private val viewModel = ViewModel

    private lateinit var events: List<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        supportActionBar?.title = "GenS"

        // Obtain data for model in storage
        viewModel.init(this)

        // register this activity to the model
        viewModel.registerView(this)

        // Get events from the viewModel
        events = viewModel.getAllEvents()

        // Get access to floating action button
        actionButton = findViewById(R.id.main_action_button)
        actionButton.setOnClickListener {
            showFabPopup()
        }

        events = listOf(
            Event("1", "Event 1",  dateTimeStringToEpoch("2023-02-03 13:00"), dateTimeStringToEpoch("2023-02-05 15:00"), color="#ae6179"),
            Event("2", "Event 2",  dateTimeStringToEpoch("2023-02-05 13:00"), dateTimeStringToEpoch("2023-02-05 15:00"), color="#6169ae"),
            Event("3", "Event 3",  dateTimeStringToEpoch("2023-02-27 13:00"), dateTimeStringToEpoch("2023-03-03 15:00"), color="#6eae61"),
            Event("4", "Event 4",  dateTimeStringToEpoch("2023-03-03 13:00"), dateTimeStringToEpoch("2023-03-05 15:00"), color="#618bae"),
            Event("5", "Event 5",  dateTimeStringToEpoch("2023-03-05 13:00"), dateTimeStringToEpoch("2023-03-05 15:00"), color="#9261ae"),
            Event("6", "Event 6",  dateTimeStringToEpoch("2023-03-30 13:00"), dateTimeStringToEpoch("2023-04-03 15:00"), color="#ae8e61"),
            Event("7", "Event 7",  dateTimeStringToEpoch("2023-04-03 13:00"), dateTimeStringToEpoch("2023-04-05 15:00"), color="#61a2ae"),
            Event("8", "Event 8",  dateTimeStringToEpoch("2023-04-05 13:00"), dateTimeStringToEpoch("2023-04-05 15:00"), color="#ae619c"),
        )

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

    @SuppressLint("SetTextI18n", "NewApi")
    private fun renderCalender() {
        val yearMonthTextView = findViewById<TextView>(R.id.calendar_year_month)
        val tableLayout = findViewById<LinearLayout>(R.id.calendar_table)
        val numberOfDays = LocalDate.of(currentYear, currentMonth + 1, 1).lengthOfMonth()

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
        while (currentDay <= numberOfDays) {
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
                // datetime.month is 1-indexed, currentMonth is 0-indexed
                val datetime = LocalDateTime.ofEpochSecond(event.startDate, 0, ZoneOffset.UTC)
                val eventStartsToday = (datetime.year == currentYear) && (datetime.month.value - 1 == currentMonth) && (datetime.dayOfMonth == currentDay)

                val startDatetime = LocalDateTime.ofEpochSecond(event.startDate, 0, ZoneOffset.UTC)
                val endDatetime = LocalDateTime.ofEpochSecond(event.endDate, 0, ZoneOffset.UTC)
                val currentDatetime = LocalDateTime.of(currentYear, currentMonth + 1, currentDay, 0, 0)
                val eventIncludesToday = (currentDatetime in startDatetime..endDatetime)

                eventStartsToday || eventIncludesToday
            }

            if (eventsForDay.isNotEmpty()) {
                // Create a new linear layout to hold the events for the day
                val eventsLayout = LinearLayout(this)
                eventsLayout.orientation = LinearLayout.VERTICAL

                // Add each event to the linear layout
                for (event in eventsForDay) {
                    val eventTextView = TextView(this)
                    eventTextView.text = event.name
                    eventTextView.gravity = Gravity.CENTER
                    eventTextView.textSize = 9f
                    eventTextView.setTextColor((Color.parseColor("#FFFFFFFF")))
                    eventTextView.setBackgroundColor((Color.parseColor(event.color)))

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

            if (currentDay > numberOfDays) {
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

    @SuppressLint("SimpleDateFormat")
    private fun dateTimeStringToEpoch(dateTimeStr: String): Long {
        return SimpleDateFormat("yyyy-MM-dd hh:mm").parse(dateTimeStr)!!.time / 1000
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
            R.id.fab_delete_calendar -> attemptDeleteCalendar()
            R.id.fab_load_test_data -> attemptLoadInitDate() // TODO: remove once we can add plans
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

    private fun attemptLoadInitDate(): Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Load Init Date")
        builder.setMessage("Loading initial data will delete existing data. This action cannot be reversed.")
        builder.setPositiveButton("Confirm") { dialog, _ ->
            viewModel.deleteCalendar()
            viewModel.loadInitialData()
            dialog.cancel()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val alert = builder.create()
        alert.show()
        return true
    }

    private fun attemptDeleteCalendar(): Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Calendar Deletion")
        builder.setMessage("Are you sure you want to delete the current Calendar? This action cannot be reversed.")
        builder.setPositiveButton("Delete") { dialog, _ ->
            viewModel.deleteCalendar()
            dialog.cancel()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val alert = builder.create()
        alert.show()
        return true
    }

    override fun update() {
        events = viewModel.getAllEvents()
        renderCalender()
    }
}