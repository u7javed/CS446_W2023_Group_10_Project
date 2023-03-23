package cs446.group10.gen_s
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import cs446.group10.gen_s.ui.activities.GeneratePlanActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class PlanBasicInfoDetail(
    val valid: Boolean,
    val planName: String,
    var startDate: DateVal?,
    var endDate: DateVal?,
)


/**
 * A simple [Fragment] subclass.
 * Use the [PlanBasicInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlanBasicInfoFragment(
    private val initialPlanBasicInfo: PlanBasicInfoDetail,
): Fragment(R.layout.fragment_plan_basic_info) {

    private var startDate: DateVal? = null;
    private var endDate: DateVal? = null;
    private var notificationOn: Boolean = false;
    private lateinit var nameTextField: EditText;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startDate = initialPlanBasicInfo.startDate;
        endDate = initialPlanBasicInfo.endDate;
        notificationOn = false;
        nameTextField = view.findViewById(R.id.PlanNameTextField);
        nameTextField.setText(initialPlanBasicInfo.planName);

        val chosenStartDate = view.findViewById<TextView>(R.id.chosenPlanStartDate);
        if (startDate != null) {
            chosenStartDate.text = DatePickerFragment.convertDateToString(startDate!!);
        }

        val chosenEndDate = view.findViewById<TextView>(R.id.chosenPlanEndDate);
        if (endDate != null) {
            chosenEndDate.text = DatePickerFragment.convertDateToString(endDate!!);
        }

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

    public fun getPlanBasicInfo(): PlanBasicInfoDetail {
        return PlanBasicInfoFragment.formPlanBasicInfo(nameTextField, startDate, endDate);
    }

    companion object {
        fun formPlanBasicInfo(planName: String, startDate: DateVal?, endDate: DateVal?): PlanBasicInfoDetail {
            var valid = planName != "" && startDate != null && endDate != null;
            return PlanBasicInfoDetail(
                valid,
                planName,
                startDate,
                endDate,
            )
        }
        fun formPlanBasicInfo(nameTextField: EditText, startDate: DateVal?, endDate: DateVal?): PlanBasicInfoDetail {
            var valid = nameTextField != null && startDate != null && endDate != null;
            valid = valid && nameTextField!!.text.toString() != "";
            return PlanBasicInfoDetail(
                valid,
                nameTextField!!.text.toString(),
                startDate,
                endDate,
            )
        }
    }
}