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
        supportActionBar?.title = "Add an Event"

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

        val spinner = findViewById<Spinner>(R.id.notificationSpinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, timeUnits
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(this@AddEventActivity, "hi", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
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
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd.MM.yyyy" // mention the format you need
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
        } else {
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