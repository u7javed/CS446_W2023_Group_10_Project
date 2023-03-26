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
    private lateinit var colorPicker: AlertDialog
    private lateinit var planNameField: EditText
    private lateinit var planNameLabel: TextView
    private lateinit var startDateText: TextView
    private lateinit var endDateText: TextView
    private lateinit var dpd: DatePickerDialog

    private var pickedColorHex: Int = 0x0
    private var planColor: String = "#ff000000"
    private var selectedTechnique: Technique = Technique.Pomodoro
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null
    private var isStartDatePicker: Boolean = true

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
        planStartDateButton = findViewById(R.id.autoGeneratePlanStartDateButton)
        planEndDateButton = findViewById(R.id.autoGeneratePlanEndDateButton)

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
        planStartDateButton.setOnClickListener {
            isStartDatePicker = true
            dpd.show()
        }
        planEndDateButton.setOnClickListener {
            isStartDatePicker = false
            dpd.show()
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
        println("COLOR Selected: $planColor")
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

        val newPlan = viewModel.addTechniquePlanToCalendar(
            planName, selectedTechnique, startDate, endDate, planColor) ?: return false
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
        super.onStop()
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}