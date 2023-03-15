package cs446.group10.gen_s

import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class DateVal(
    val year: Int,
    val month: Int,
    val day: Int,
)

data class DatePickerInitialVal(
    val onSet: (DateVal)->Unit,
    val dateVal: DateVal?,
)

/**
 *
 * To use the date picker, add the following to the xml file
 *
 * <FrameLayout
        android:id="@+id/fragmentPlaceholder"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
 *
 * Then add the following to the onViewCreated function of the corresponding kt file
 *  val datePicker = DatePickerFragment();
 *  activity?.supportFragmentManager?.beginTransaction()?.apply {
        replace(R.id.fragmentPlaceholder, datePicker);
        commit();
    }
 * The above lines replace what's inside the FrameLayout with the DatePicker
 * However, the DatePicker will not appear until datePicker.showDatePicker() is called.
 *
 * To make the datePicker appear, create an function that defines what to do after the date is selected
 * and execute the showDatePicker function. E.g.
 *
 *  fun setStartDate(dateVal: DateVal) { // this is the function that defines what to do with the selected date
        val chosenStartDate = view.findViewById<TextView>(R.id.chosenStartDate);
        chosenStartDate.text = DatePickerFragment.convertDateToString(dateVal);
    }

    val selectStartDateButton = view.findViewById<MaterialButton>(R.id.selectStartDate);
    selectStartDateButton.setOnClickListener {
        startDateCalendarFragment.showDatePicker(
            DatePickerInitialVal(::setStartDate, null) // function mentioned above and selected date
            // replace null with the selected date if date is selected
            // using null will show today's date when the DatePicker appear
        );
    }
 *
 */



/**
 * A simple [Fragment] subclass.
 * Use the [DatePickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatePickerFragment : Fragment(R.layout.fragment_date_picker) {
    public fun showDatePicker(initVal: DatePickerInitialVal) {
        var cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)

        if (initVal.dateVal != null) {
            year = initVal.dateVal.year;
            month = initVal.dateVal.month;
            day = initVal.dateVal.day;
        }


        val dpd = context?.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    initVal.onSet(DateVal(year, monthOfYear, dayOfMonth))
                },
                year,
                month,
                day
            )
        }
        if (dpd != null) {
            dpd.show()
        }
    }

    companion object {
        fun convertDateToString(dateVal: DateVal): String {
            val myFormat = "dd.MM.yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            var cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, dateVal.year)
            cal.set(Calendar.MONTH, dateVal.month)
            cal.set(Calendar.DAY_OF_MONTH, dateVal.day)
            return sdf.format(cal.time);
        }

        fun convertDateToLocalDate(dateVal: DateVal?): LocalDate {
            if (dateVal == null) {
                return LocalDate.parse("");
            }
            val format = "dd-MM-yyyy"
            var formatter = DateTimeFormatter.ofPattern(format)
            val sdf = SimpleDateFormat(format, Locale.US)

            var cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, dateVal.year)
            cal.set(Calendar.MONTH, dateVal.month)
            cal.set(Calendar.DAY_OF_MONTH, dateVal.day)
            val dateStr = sdf.format(cal.time);

            return LocalDate.parse(dateStr, formatter)
        }
    }
}