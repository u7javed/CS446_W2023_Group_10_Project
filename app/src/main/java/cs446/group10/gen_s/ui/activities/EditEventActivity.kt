package cs446.group10.gen_s.ui.activities

import cs446.group10.gen_s.backend.view_model.ViewModel
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.dataClasses.Event
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class EditEventActivity : AppCompatActivity() {

    private val _viewModel = ViewModel
    private val _dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val _timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        // Exit activity if cancel is clicked
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            finish()
        }

        var eventName = findViewById<EditText>(R.id.eventNameInput)
        var startDate = findViewById<TextView>(R.id.chosenStartDate)
        var endDate = findViewById<TextView>(R.id.chosenEndDate)
        var startTime = findViewById<TextView>(R.id.chosenStartTime)
        var endTime = findViewById<TextView>(R.id.chosenEndTime)

        val extras = intent.extras
        if (extras != null) {
            val eventId: String = extras.getString("eventId")!!
            val event: Event? = _viewModel.getEventById(eventId)
            if (event != null) {
                // Get event information
                val startDateTime = LocalDateTime.ofEpochSecond(event.startDate, 0, ZoneOffset.UTC)
                val startDateStr = startDateTime.toLocalDate().format(_dateFormatter)
                val startTimeStr = startDateTime.toLocalTime().format(_timeFormatter)
                val endDateTime = LocalDateTime.ofEpochSecond(event.endDate, 0, ZoneOffset.UTC)
                val endDateStr = endDateTime.toLocalDate().format(_dateFormatter)
                val endTimeStr = endDateTime.toLocalTime().format(_timeFormatter)

                eventName.hint = event.name
                startDate.text = startDateStr
                endDate.text = endDateStr
                // TODO: Update based on the start date and end date information
                startTime.text = startTimeStr
                endTime.text = endTimeStr
            }
        }
    }

}