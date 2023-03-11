package cs446.group10.gen_s.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cs446.group10.gen_s.R
import java.text.SimpleDateFormat
import java.util.*


class AddEventActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnStartDatePicker: Button;
    private lateinit var btnStartTimePicker: Button;
    private lateinit var btnEndDatePicker: Button;
    private lateinit var btnEndTimePicker: Button;
    private lateinit var startDateText: TextView;
    private lateinit var startTimeText: TextView;
    private lateinit var endDateText: TextView;
    private lateinit var endTimeText: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Add an cs446.group10.gen_s.backend.dataClasses.Event"

        btnStartDatePicker = findViewById<Button>(R.id.startDateInput)
        btnStartTimePicker = findViewById<Button>(R.id.startTimeInput)
        btnEndDatePicker = findViewById<Button>(R.id.endDateInput)
        btnEndTimePicker = findViewById<Button>(R.id.endTimeInput)
        startDateText = findViewById(R.id.chosenStartDate)
        startTimeText = findViewById(R.id.chosenStartTime)
        endDateText = findViewById(R.id.chosenEndDate)
        endTimeText = findViewById(R.id.chosenEndTime)

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
        notificationSwitch?.setOnCheckedChangeListener({ _ , isChecked ->
            if (!isChecked) {
                notificationSpinner.visibility = View.GONE
                notificationInput.visibility = View.GONE
            } else {
                notificationSpinner.visibility = View.VISIBLE
                notificationInput.visibility = View.VISIBLE
            }
        })

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
        associatedPlanSwitch?.setOnCheckedChangeListener({ _ , isChecked ->
            if (!isChecked) {
                studyPlanSpinner.visibility = View.GONE
            } else {
                studyPlanSpinner.visibility = View.VISIBLE
            }
        })
    }

    override fun onClick(v: View?) {
        if (v == btnStartDatePicker || v == btnEndDatePicker) {
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd.MM.yyyy"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    if (v == btnStartDatePicker) {
                        startDateText.setText(sdf.format(cal.time))
                    } else {
                        endDateText.setText(sdf.format(cal.time))
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
                    { timePicker, i, i1 ->
                        if (v == btnStartTimePicker) {
                            startTimeText.setText(i.toString() + "H:," + i1 + "m")
                        } else {
                            endTimeText.setText(i.toString() + "H:," + i1 + "m")
                        }
                    }, dH, dMin, false
                )
                tpd.show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}