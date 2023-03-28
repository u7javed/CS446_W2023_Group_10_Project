package cs446.group10.gen_s.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.dataClasses.Plan
import cs446.group10.gen_s.backend.techniques.Technique
import cs446.group10.gen_s.backend.view_model.ViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*

class AutoGenerateActivity : AppCompatActivity() {

    private lateinit var notificationDetails: LinearLayout
    private lateinit var notificationSwitch: Switch
    private lateinit var techniqueSpinner: Spinner
    private lateinit var generatePlanButton: Button
    private lateinit var selectColorButton: Button
    private lateinit var selectedColorBox: View
    private lateinit var planStartDateButton: Button
    private lateinit var planEndDateButton: Button
    private lateinit var planStartTimeButton: Button
    private lateinit var planEndTimeButton: Button
    private lateinit var colorPicker: AlertDialog
    private lateinit var planNameField: EditText
    private lateinit var planNameLabel: TextView
    private lateinit var startDateText: TextView
    private lateinit var endDateText: TextView
    private lateinit var startTimeText: TextView
    private lateinit var endTimeText: TextView
    private lateinit var dpd: DatePickerDialog
    private lateinit var tpd: TimePickerDialog

    private var pickedColorHex: Int = 0x0
    private var planColor: String = "#ff000000"
    private var selectedTechnique: Technique = Technique.Pomodoro
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null
    private var startTime: LocalTime = LocalTime.of(8, 0)
    private var endTime: LocalTime = LocalTime.of(23, 59)
    private var isStartDatePicker: Boolean = true
    private var isStartTimePicker: Boolean = true

    private val viewModel = ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_generate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Auto-Generate a Plan"

        colorPicker = ColorPickerDialogBuilder
            .with(this)
            .setTitle("Choose color for your plan!")
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorChangedListener { colorInt ->
                pickedColorHex = colorInt
            }
            .setPositiveButton("Select") { _, selectedColor, _ ->
                planColor = "#${Integer.toHexString(selectedColor)}"
                selectedColorBox.setBackgroundColor(selectedColor)

            }.setNegativeButton("Cancel") { _, _ ->
            }
            .build()

        selectedColorBox = findViewById(R.id.colorSelectedView)
        notificationDetails = findViewById(R.id.autoGenerateNotificationDetails)
        notificationSwitch = findViewById(R.id.autoGenerateNotificationSwitch)
        selectColorButton = findViewById(R.id.selectColorButton)
        techniqueSpinner = findViewById(R.id.autoGenerateTechniqueSpinner)
        planNameField = findViewById(R.id.autoGeneratePlanNameTextField)
        planNameLabel = findViewById(R.id.autoGeneratePlanNameLabel)
        generatePlanButton = findViewById(R.id.autoGenerateButton)
        startDateText = findViewById(R.id.autoGenerateChosenPlanStartDate)
        endDateText = findViewById(R.id.autoGenerateChosenPlanEndDate)
        startTimeText = findViewById(R.id.autoGenerateChosenPlanStartTime)
        endTimeText = findViewById(R.id.autoGenerateChosenPlanEndTime)
        planStartDateButton = findViewById(R.id.autoGeneratePlanStartDateButton)
        planEndDateButton = findViewById(R.id.autoGeneratePlanEndDateButton)
        planStartTimeButton = findViewById(R.id.autoGeneratePlanStartTimeButton)
        planEndTimeButton = findViewById(R.id.autoGeneratePlanEndTimeButton)

        selectColorButton.setOnClickListener {
            colorPicker.show()
        }

        // Fill up technique spinner
        val techniques: List<Technique> = Technique.values().toList()
        val values = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, techniques)
        techniqueSpinner.adapter = values
        techniqueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedTechnique = techniques[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedTechnique = Technique.Pomodoro
            }

        }

        // Set switch off as default
        notificationSwitch.isChecked = false
        notificationDetails.visibility = View.GONE

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                notificationDetails.visibility = View.VISIBLE
            } else {
                notificationDetails.visibility = View.GONE
            }
        }

        // Set date pick dialog
        dpd = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                if (isStartDatePicker) {
                    startDate = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                    startDateText.text = startDate.toString()
                } else {
                    endDate = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                    endDateText.text = endDate.toString()
                }
            },
            LocalDate.now().year,
            LocalDate.now().monthValue-1,
            LocalDate.now().dayOfMonth
        )

        startTimeText.text = "${startTime.hour}h:${startTime.minute}m"
        endTimeText.text = "${endTime.hour}h:${endTime.minute}m"
        tpd = TimePickerDialog(
            this,
            { _, hour, minute ->
                if (isStartTimePicker) {
                    startTime = LocalTime.of(hour, minute)
                    startTimeText.text = "${hour}h:${minute}m"
                } else {
                    endTime = LocalTime.of(hour, minute)
                    endTimeText.text = "${hour}h:${minute}m"
                }
            },
            LocalTime.now().hour,
            LocalTime.now().minute,
            false
        )

        planStartDateButton.setOnClickListener {
            isStartDatePicker = true
            dpd.show()
        }
        planEndDateButton.setOnClickListener {
            isStartDatePicker = false
            dpd.show()
        }

        planStartTimeButton.setOnClickListener {
            isStartTimePicker = true
            tpd.show()
        }
        planEndTimeButton.setOnClickListener {
            isStartTimePicker = false
            tpd.show()
        }
        // Assign generate button click response
        generatePlanButton.setOnClickListener {
            val res: Boolean = attemptPlanCreation()
            if (res)
                finish()
            else
                errorToast()
        }
    }

    private fun attemptPlanCreation(): Boolean {
        if (planNameField.text.toString().isEmpty()) {
            planNameLabel.setTextColor(Color.RED)
            return false
        }
        if (startDate == null || endDate == null) {
            return false
        }
        val planName: String = planNameField.text.toString()
        val startDate: Long = LocalDateTime.of(startDate!!, LocalTime.MIDNIGHT).toEpochSecond(
            ZoneOffset.UTC)
        val endDate: Long = LocalDateTime.of(endDate!!, LocalTime.MIDNIGHT).toEpochSecond(
            ZoneOffset.UTC)

        // Get restrictions
        if (startTime.isAfter(endTime))
            return false

        val dayRestrictions: Pair<LocalTime, LocalTime> = Pair(startTime, endTime)

        val newPlan = viewModel.addTechniquePlanToCalendar(
            planName, selectedTechnique, startDate, endDate, dayRestrictions, planColor) ?: return false
        return true
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Error! Unable to generate Plan. Please ensure time ranges are valid!",
            Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        colorPicker.dismiss()
        dpd.dismiss()
        tpd.dismiss()
        super.onStop()
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}