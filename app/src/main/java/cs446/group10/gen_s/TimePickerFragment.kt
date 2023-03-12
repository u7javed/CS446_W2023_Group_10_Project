package cs446.group10.gen_s

import android.app.TimePickerDialog
import androidx.fragment.app.Fragment
import java.util.*

data class TimeVal(
    val hour: Int,
    val minute: Int,
)

data class TimePickerInitialVal(
    val onSet: (TimeVal)->Unit,
    val timeVal: TimeVal?,
)

/**
 * A simple [Fragment] subclass.
 * Use the [TimePickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimePickerFragment : Fragment(R.layout.fragment_time_picker) {
    public fun showTimePicker(initVal: TimePickerInitialVal) {
        var dH: Int
        var dMin: Int
        val c = Calendar.getInstance()
        dH = c[Calendar.HOUR_OF_DAY]
        dMin = c[Calendar.MINUTE]

        if (initVal.timeVal != null) {
            dH = initVal.timeVal.hour;
            dMin = initVal.timeVal.hour;
        }

        val tpd = context?.let {
            TimePickerDialog(
                it,
                { _, hour, minute ->
                    initVal.onSet(TimeVal(hour, minute))
                }, dH, dMin, false
            )
        }
        if (tpd != null) {
            tpd.show()
        }
    }

    companion object {
        fun convertTimeToString(timeVal: TimeVal): String {
            val hStr = if (timeVal.hour < 10) "0${timeVal.hour}" else "${timeVal.hour}";
            val minStr = if (timeVal.minute < 10) "0${timeVal.minute}" else "${timeVal.minute}";
            return "${hStr}:${minStr}";
        }
    }
}