package cs446.group10.gen_s.ui.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cs446.group10.gen_s.R

class EditEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)
        var eventName = findViewById<EditText>(R.id.eventNameInput)
        var startDate = findViewById<TextView>(R.id.chosenStartDate)
        var endDate = findViewById<TextView>(R.id.chosenEndDate)
        var startTime = findViewById<TextView>(R.id.chosenStartTime)
        var endTime = findViewById<TextView>(R.id.chosenEndTime)

        val extras = intent.extras
        if (extras != null) {
            var eventNameVal = extras.getString("eventName")
            var startDateVal = extras.getString("date")
            var endDateVal = extras.getString("date")
            var startTimeVal = extras.getString("startTime")
            var endTimeVal = extras.getString("endTime")
            eventName.hint = eventNameVal
            startDate.text = startDateVal
            endDate.text = endDateVal
            startTime.text = startTimeVal
            endTime.text = endTimeVal
        }
    }

}