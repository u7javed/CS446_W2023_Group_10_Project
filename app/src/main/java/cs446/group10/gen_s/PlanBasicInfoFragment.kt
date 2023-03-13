package cs446.group10.gen_s
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton


/**
 * A simple [Fragment] subclass.
 * Use the [PlanBasicInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlanBasicInfoFragment : Fragment(R.layout.fragment_plan_basic_info) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var startDate: DateVal? = null;
        var endDate: DateVal? = null;
        var notificationOn: Boolean = false;

        // start date

        val startDateCalendarFragment = DatePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.PlanStartDatePickerFragment, startDateCalendarFragment);
            commit();
        }

        fun setStartDate(dateVal: DateVal) {
            val chosenStartDate = view.findViewById<TextView>(R.id.chosenPlanStartDate);
            startDate = dateVal;
            chosenStartDate.text = DatePickerFragment.convertDateToString(dateVal);
        }

        val selectStartDateButton = view.findViewById<MaterialButton>(R.id.selectPlanStartDate);
        selectStartDateButton.setOnClickListener {
            startDateCalendarFragment.showDatePicker(
                DatePickerInitialVal(::setStartDate, startDate)
            );
        }

        // end date

        val endDateCalendarFragment = DatePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.PlanEndDatePickerFragment, endDateCalendarFragment);
            commit();
        }

        fun setEndDate(dateVal: DateVal) {
            val chosenEndDate = view.findViewById<TextView>(R.id.chosenPlanEndDate);
            endDate = dateVal;
            chosenEndDate.text = DatePickerFragment.convertDateToString(dateVal);
        }

        val selectEndDateButton = view.findViewById<MaterialButton>(R.id.selectPlanEndDate);
        selectEndDateButton.setOnClickListener {
            endDateCalendarFragment.showDatePicker(
                DatePickerInitialVal(::setEndDate, endDate)
            );
        }

        // notification
        val notificationSwitch = view.findViewById<Switch>(R.id.NotificationSwitch);
        notificationSwitch.isChecked = notificationOn;

        val notificationDetails = view.findViewById<LinearLayout>(R.id.NotificationDetails);
            notificationDetails.visibility = if (notificationOn) View.VISIBLE else View.GONE;

        notificationSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            notificationOn = isChecked;
            notificationDetails.visibility = if (notificationOn) View.VISIBLE else View.GONE;
        })

        val timeUnits = resources.getStringArray(R.array.UnitsOfTime);
        val timeUnitsSpinner = view.findViewById<Spinner>(R.id.NotificationUnit)
        if (timeUnitsSpinner != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item, timeUnits
                )
            }
            timeUnitsSpinner.adapter = adapter;
        }
    }
}