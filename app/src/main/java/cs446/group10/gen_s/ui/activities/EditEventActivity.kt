package cs446.group10.gen_s.ui.activities

import ViewModel
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.dataClasses.Event

class EditEventActivity : AppCompatActivity() {

    private val _viewModel: ViewModel by lazy {
        ViewModelProvider(this)[ViewModel::class.java]
    }

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
            val eventId: String = extras.getString("eventId")!!
            val event: Event? = _viewModel.getEventById(eventId)
            if (event != null) {
                eventName.hint = event.name
                startDate.text = event.startDate.toString()
                endDate.text = event.endDate.toString()
                // TODO: Update based on the start date and end date information
                startTime.text = "Something hrs"
                endTime.text = "Something scd"
            }
        }
    }

}