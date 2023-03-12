package cs446.group10.gen_s.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import cs446.group10.gen_s.backend.view_model.ViewModel
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.dataClasses.Event
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class EditEventActivity : AppCompatActivity(), View.OnClickListener {

    private val _viewModel = ViewModel
    private lateinit var btnStartDatePicker: Button;
    private lateinit var btnStartTimePicker: Button;
    private lateinit var btnEndDatePicker: Button;
    private lateinit var btnEndTimePicker: Button;
    private lateinit var deleteEvent: ImageButton;
    private lateinit var confirmEditEvent: Button;

    private lateinit var startDate: TextView;
    private lateinit var endDate: TextView;
    private lateinit var startTime: TextView;
    private lateinit var endTime: TextView;

    private lateinit var _eventNameText: EditText
    private var _startDate: LocalDate? = null
    private var _startTime: LocalTime? = null
    private var _endDate: LocalDate? = null
    private var _endTime: LocalTime? = null
    private var _notification: Long? = null



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

        deleteEvent = findViewById<ImageButton>(R.id.deleteEventButton)
        deleteEvent.setOnClickListener {
            //TO-DO: set up delete functionality
        }

        confirmEditEvent = findViewById<Button>(R.id.confirmEventButton)
        confirmEditEvent.setOnClickListener {
            //TO-DO: set up confirm edits
        }

        btnStartDatePicker = findViewById<Button>(R.id.startDateInput)
        btnStartTimePicker = findViewById<Button>(R.id.startTimeInput)
        btnEndDatePicker = findViewById<Button>(R.id.endDateInput)
        btnEndTimePicker = findViewById<Button>(R.id.endTimeInput)
        var eventName = findViewById<EditText>(R.id.eventNameInput)
        startDate = findViewById<TextView>(R.id.chosenStartDate)
        endDate = findViewById<TextView>(R.id.chosenEndDate)
        startTime = findViewById<TextView>(R.id.chosenStartTime)
        endTime = findViewById<TextView>(R.id.chosenEndTime)

        btnStartDatePicker.setOnClickListener(this)
        btnStartTimePicker.setOnClickListener(this)
        btnEndDatePicker.setOnClickListener(this)
        btnEndTimePicker.setOnClickListener(this)

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

                eventName.setText(event.name)
                startDate.text = startDateStr
                endDate.text = endDateStr
                // TODO: Update based on the start date and end date information
                startTime.text = startTimeStr
                endTime.text = endTimeStr
            }
        }

        val timeUnits = resources.getStringArray(R.array.UnitsOfTime)

        val notificationInput = findViewById<EditText>(R.id.notificationValue)
        val notificationSpinner = findViewById<Spinner>(R.id.notificationSpinner)

        if (notificationSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, timeUnits
            )
            notificationSpinner.adapter = adapter

            notificationSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    //TO DO: integrate with backend
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //TO DO: integrate with backend
                }
            }
        }

        var notificationSwitch = findViewById<Switch>(R.id.notificationSwitchEvent)
        notificationSwitch.isChecked = true
        notificationSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                notificationSpinner.visibility = View.GONE
                notificationInput.visibility = View.GONE
            } else {
                notificationSpinner.visibility = View.VISIBLE
                notificationInput.visibility = View.VISIBLE
            }
        }

        //TO-DO: connect to backend
        val currentPlans = resources.getStringArray(R.array.TempPlans)
        val studyPlanSpinner = findViewById<Spinner>(R.id.studyPlanSpinner)

        if (studyPlanSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, currentPlans
            )
            studyPlanSpinner.adapter = adapter

            studyPlanSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    //TO DO: integrate with backend

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //TO DO: integrate with backend
                }
            }
        }

        var associatedPlanSwitch = findViewById<Switch>(R.id.associatedPlanSwitch)
        associatedPlanSwitch.isChecked = true
        associatedPlanSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                studyPlanSpinner.visibility = View.GONE
            } else {
                studyPlanSpinner.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == btnStartDatePicker || v == btnEndDatePicker) {
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "dd.MM.yyyy"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    if (v == btnStartDatePicker) {
                        _startDate = LocalDate.of(year, month, dayOfMonth)
                        startDate.setText(sdf.format(cal.time))
                    } else {
                        _endDate = LocalDate.of(year, month, dayOfMonth)
                        endDate.setText(sdf.format(cal.time))
                    }
                },
                year,
                month,
                day
            )
            dpd.show()
        } else if (v == btnStartTimePicker || v == btnEndTimePicker) {
            val dH: Int
            val dMin: Int
            val c = Calendar.getInstance()
            dH = c[Calendar.HOUR]
            dMin = c[Calendar.MINUTE]
            val tpd = TimePickerDialog(
                this,
                { _, hour, minute ->
                    if (v == btnStartTimePicker) {
                        _startTime = LocalTime.of(hour, minute)
                        startTime.text = "${hour}h:,${minute}m"
                    } else {
                        _endTime = LocalTime.of(hour, minute)
                        endTime.text = "${hour}h:,${minute}m"
                    }
                }, dH, dMin, false
            )
            tpd.show()
        }
    }

}