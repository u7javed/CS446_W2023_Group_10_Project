package cs446.group10.gen_s.ui.activities

import cs446.group10.gen_s.backend.view_model.ViewModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cs446.group10.gen_s.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*


class AddEventActivity : AppCompatActivity(), View.OnClickListener {

    private val _viewModel = ViewModel
    private lateinit var btnStartDatePicker: Button;
    private lateinit var btnStartTimePicker: Button;
    private lateinit var btnEndDatePicker: Button;
    private lateinit var btnEndTimePicker: Button;
    private lateinit var startDateText: TextView;
    private lateinit var startTimeText: TextView;
    private lateinit var endDateText: TextView;
    private lateinit var endTimeText: TextView;

    private lateinit var _eventNameText: EditText
    private var _startDate: LocalDate? = null
    private var _startTime: LocalTime? = null
    private var _endDate: LocalDate? = null
    private var _endTime: LocalTime? = null
    private var _notification: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Add an event"

        btnStartDatePicker = findViewById<Button>(R.id.startDateInput)
        btnStartTimePicker = findViewById<Button>(R.id.startTimeInput)
        btnEndDatePicker = findViewById<Button>(R.id.endDateInput)
        btnEndTimePicker = findViewById<Button>(R.id.endTimeInput)
        startDateText = findViewById(R.id.chosenStartDate)
        startTimeText = findViewById(R.id.chosenStartTime)
        endDateText = findViewById(R.id.chosenEndDate)
        endTimeText = findViewById(R.id.chosenEndTime)

        _eventNameText = findViewById(R.id.eventNameInput)

        // Add an event listener to quit this activity if cancel button is clicked
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            finish()
        }

        // Add an event listener to confirm to create an activity in the model
        val confirmButton = findViewById<Button>(R.id.confirmEventButton)
        confirmButton.setOnClickListener {
            val res: Boolean = createEvent()
            if (res)
                finish()
            else
                errorToast()
        }

        btnStartDatePicker.setOnClickListener(this)
        btnStartTimePicker.setOnClickListener(this)
        btnEndDatePicker.setOnClickListener(this)
        btnEndTimePicker.setOnClickListener(this)

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

    @SuppressLint("SetTextI18n")
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
                        _startDate = LocalDate.of(year, month+1, dayOfMonth)
                        startDateText.text = sdf.format(cal.time)
                    } else {
                        _endDate = LocalDate.of(year, month+1, dayOfMonth)
                        endDateText.text = sdf.format(cal.time)
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
                            startTimeText.text = "${hour}h:,${minute}m"
                        } else {
                            _endTime = LocalTime.of(hour, minute)
                            endTimeText.text = "${hour}h:,${minute}m"
                        }
                    }, dH, dMin, false
                )
                tpd.show()
            }
    }

    private fun createEvent(): Boolean {
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
        // TODO: Deal with notification
        return _viewModel.addEventToCalendar(eventName, startDate, endDate, null)
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Error! Make sure there are no conflicting events and the " +
                    "start and end dates/times are valid.",
            Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}