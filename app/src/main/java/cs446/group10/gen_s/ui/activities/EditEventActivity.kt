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
import cs446.group10.gen_s.backend.dataClasses.Plan
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
    private lateinit var _eventId: String

    private lateinit var _eventNameText: EditText
    private var _startDate: LocalDate? = null
    private var _startTime: LocalTime? = null
    private var _endDate: LocalDate? = null
    private var _endTime: LocalTime? = null
    private var _planId: String? = null
    private var _planIsChecked: Boolean = false
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

        btnStartDatePicker = findViewById<Button>(R.id.startDateInput)
        btnStartTimePicker = findViewById<Button>(R.id.startTimeInput)
        btnEndDatePicker = findViewById<Button>(R.id.endDateInput)
        btnEndTimePicker = findViewById<Button>(R.id.endTimeInput)
        var eventName = findViewById<EditText>(R.id.eventNameInput)
        startDate = findViewById<TextView>(R.id.chosenStartDate)
        endDate = findViewById<TextView>(R.id.chosenEndDate)
        startTime = findViewById<TextView>(R.id.chosenStartTime)
        endTime = findViewById<TextView>(R.id.chosenEndTime)

        _eventNameText = findViewById(R.id.eventNameInput)

        btnStartDatePicker.setOnClickListener(this)
        btnStartTimePicker.setOnClickListener(this)
        btnEndDatePicker.setOnClickListener(this)
        btnEndTimePicker.setOnClickListener(this)

        val extras = intent.extras
        if (extras != null) {
            val eventId: String = extras.getString("eventId")!!
            _eventId = eventId
            val event: Event? = _viewModel.getEventById(eventId)
            if (event != null) {
                // Get event information
                val startDateTime = LocalDateTime.ofEpochSecond(event.startDate, 0, ZoneOffset.UTC)
                _startDate = startDateTime.toLocalDate()
                _startTime = startDateTime.toLocalTime()
                val startDateStr = startDateTime.toLocalDate().format(_dateFormatter)
                val startTimeStr = startDateTime.toLocalTime().format(_timeFormatter)
                val endDateTime = LocalDateTime.ofEpochSecond(event.endDate, 0, ZoneOffset.UTC)
                _endDate = endDateTime.toLocalDate()
                _endTime = endDateTime.toLocalTime()
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

        deleteEvent = findViewById<ImageButton>(R.id.deleteEventButton)
        deleteEvent.setOnClickListener {
            val res: Boolean = deleteEvent(_eventId)
            if (res)
                finish()
            else
                couldNotDeleteToast()
        }

        confirmEditEvent = findViewById<Button>(R.id.confirmEventUpdateButton)
        confirmEditEvent.setOnClickListener {
            val res: Boolean = updateEvent()
            if (res)
                finish()
            else
                errorToast()
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

        var associatedPlanSwitch = findViewById<Switch>(R.id.associatedPlanSwitch)

        //TO-DO: connect to backend
        val currentPlans: MutableList<PlanSpinner> = mutableListOf()
        _viewModel.getAllPlans().forEach { plan ->
            currentPlans.add(PlanSpinner(plan.planId, plan.name))
        }

        val studyPlanSpinner = findViewById<Spinner>(R.id.studyPlanSpinner)

        if (studyPlanSpinner != null && currentPlans.size != 0) {
            associatedPlanSwitch.isChecked = true
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
                    _planId = currentPlans[position].planId

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    _planId = null
                }
            }
        } else if (currentPlans.size == 0) {
            studyPlanSpinner.visibility = View.GONE
            associatedPlanSwitch.isChecked = false
            associatedPlanSwitch.isClickable = false
        }

        _planIsChecked = true
        associatedPlanSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                studyPlanSpinner.visibility = View.GONE
            } else {
                studyPlanSpinner.visibility = View.VISIBLE
            }
            _planIsChecked = isChecked
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
                        _startDate = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                        startDate.text = sdf.format(cal.time)
                    } else {
                        _endDate = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                        endDate.text = sdf.format(cal.time)
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
                        startTime.text = "${hour}h:${minute}m"
                    } else {
                        _endTime = LocalTime.of(hour, minute)
                        endTime.text = "${hour}h:${minute}m"
                    }
                }, dH, dMin, false
            )
            tpd.show()
        }
    }

    private fun updateEvent(): Boolean {
        if (_eventNameText.text.toString().isEmpty() || _startDate == null || _endDate == null ||
            _startTime == null || _endTime == null
        ) {
            return false
        }
        val eventName: String = _eventNameText.text.toString()
        val startDate: Long =
            LocalDateTime.of(_startDate!!, _startTime!!).toEpochSecond(ZoneOffset.UTC)
        val endDate: Long = LocalDateTime.of(_endDate!!, _endTime!!).toEpochSecond(ZoneOffset.UTC)
        if (startDate >= endDate) {
            return false
        }
        var planId: String? = null
        if (_planIsChecked && _planId != null)
            planId = _planId
        // TODO: Deal with notification
        return _viewModel.updateEventInCalendar(_eventId, eventName, startDate, endDate, null, planId)
    }

    private fun deleteEvent(eventId: String): Boolean {
        return _viewModel.deleteEventInCalendar(eventId)
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Error! Make sure there are no conflicting events and the " +
                    "start and end dates/times are valid.",
            Toast.LENGTH_SHORT).show()
    }

    private fun couldNotDeleteToast() {
        Toast.makeText(
            this,
            "Failed to delete Event $_eventId",
            Toast.LENGTH_SHORT).show()
    }

    class PlanSpinner (
        val planId: String,
        val planName: String) {

        override fun toString(): String {
            return planName
        }

    }

}