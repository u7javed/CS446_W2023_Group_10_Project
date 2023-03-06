package cs446.group10.gen_s.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cs446.group10.gen_s.R
import java.text.SimpleDateFormat
import java.util.*


class AddEventActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnStartDatePicker: Button;
    private lateinit var btnStartTimePicker: Button;
    private lateinit var btnEndDatePicker: Button;
    private lateinit var btnEndTimePicker: Button;

    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("MMMM d, yyyy hh:mm:ss a", Locale.US)
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
        btnStartDatePicker.setOnClickListener(this)
        btnStartTimePicker.setOnClickListener(this)
        btnEndDatePicker.setOnClickListener(this)
        btnEndTimePicker.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == btnStartDatePicker || v == btnEndDatePicker) {
            var cal = Calendar.getInstance()
            val year = 0
            val month = 0
            val day = 0
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd.MM.yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    // Display Selected date in textbox
                    Toast.makeText(this, sdf.format(cal.time), Toast.LENGTH_LONG).show()

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
                        Toast.makeText(
                            this,
                            i.toString() + "H:," + i1 + "m",
                            Toast.LENGTH_SHORT
                        ).show()
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