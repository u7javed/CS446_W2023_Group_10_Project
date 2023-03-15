package cs446.group10.gen_s
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import cs446.group10.gen_s.ui.activities.GeneratePlanActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

data class DurationVal(
    val quantity: String,
    val unit: String,
)

data class PlanPreferenceDetail(
    val preferenceName: String,
    val startDate: DateVal,
    val endDate: DateVal,
    val startTime: TimeVal,
    val endTime: TimeVal,
//    val startDateYear: Int,
//    val startDateMonth: Int,
//    val startDateDay: Int,
//    val endDateYear: Int,
//    val endDateMonth: Int,
//    val endDateDay: Int,
//    val startTimeHour: Int,
//    val startTimeMin: Int,
//    val endTimeHour: Int,
//    val endTimeMin: Int,
    val duration: DurationVal,
)

data class PlanPreferenceInitialVal(
    val newPreference: Boolean,
    val preferenceDetail: PlanPreferenceDetail?, // should not be null if newPreference is false
)

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateEditPreferenceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateEditPreferenceFragment(
    private val preferenceDetails: PlanPreferenceInitialVal,
    private val generatePlanActivity: GeneratePlanActivity,
) :
    Fragment(R.layout.fragment_generate_edit_preference) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val newPreference = this.preferenceDetails.newPreference;

        val PreferenceName = view.findViewById<EditText>(R.id.PreferenceNameTextField);

        var startDate: DateVal? = null;
        var endDate: DateVal? = null;
        var startTime: TimeVal? = null;
        var endTime: TimeVal? = null;

        if (newPreference) {
            // will be removed later
            // used to test passing parameters from one fragment (GenerateFragment) to another fragment (GenerateEditPreferenceFragment)
            PreferenceName.setText("");
        } else {
            // set variables
        }

        // start date

        val startDateCalendarFragment = DatePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.StartDatePickerFragment, startDateCalendarFragment);
            commit();
        }

        fun setStartDate(dateVal: DateVal) {
            val chosenStartDate = view.findViewById<TextView>(R.id.chosenStartDate);
            startDate = dateVal;
            chosenStartDate.text = DatePickerFragment.convertDateToString(dateVal);
        }

        val selectStartDateButton = view.findViewById<MaterialButton>(R.id.selectStartDate);
        selectStartDateButton.setOnClickListener {
            startDateCalendarFragment.showDatePicker(
                DatePickerInitialVal(::setStartDate, startDate)
            );
        }

        // end date

        val endDateCalendarFragment = DatePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.EndDatePickerFragment, endDateCalendarFragment);
            commit();
        }

        fun setEndDate(dateVal: DateVal) {
            val chosenEndDate = view.findViewById<TextView>(R.id.chosenEndDate);
            endDate = dateVal;
            chosenEndDate.text = DatePickerFragment.convertDateToString(dateVal);
        }

        val selectEndDateButton = view.findViewById<MaterialButton>(R.id.selectEndDate);
        selectEndDateButton.setOnClickListener {
            endDateCalendarFragment.showDatePicker(
                DatePickerInitialVal(::setEndDate, endDate)
            );
        }

        // start time

        val startTimeCalendarFragment = TimePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.StartTimePickerFragment, startTimeCalendarFragment);
            commit();
        }

        fun setStartTime(timeVal: TimeVal) {
            val chosenStartTime = view.findViewById<TextView>(R.id.chosenStartTime);
            startTime = timeVal;
            chosenStartTime.text = TimePickerFragment.convertTimeToString(timeVal);
        }

        val selectStartTimeButton = view.findViewById<MaterialButton>(R.id.selectStartTime);
        selectStartTimeButton.setOnClickListener {
            startTimeCalendarFragment.showTimePicker(
                TimePickerInitialVal(::setStartTime, startTime)
            );
        }

        // end time
        val endTimeCalendarFragment = TimePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.EndTimePickerFragment, endTimeCalendarFragment);
            commit();
        }

        fun setEndTime(timeVal: TimeVal) {
            val chosenEndTime = view.findViewById<TextView>(R.id.chosenEndTime);
            endTime = timeVal;
            chosenEndTime.text = TimePickerFragment.convertTimeToString(timeVal);
        }

        val selectEndTimeButton = view.findViewById<MaterialButton>(R.id.selectEndTime);
        selectEndTimeButton.setOnClickListener {
            endTimeCalendarFragment.showTimePicker(
                TimePickerInitialVal(::setEndTime, endTime)
            );
        }

        val timeUnits = resources.getStringArray(R.array.UnitsOfDuration);
        val timeUnitsSpinner = view.findViewById<Spinner>(R.id.DurationUnit)
        if (timeUnitsSpinner != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item, timeUnits
                )
            }
            timeUnitsSpinner.adapter = adapter;
        }





        val confirmPreferenceButton = view.findViewById<MaterialButton>(R.id.ConfirmPreferenceButton);
        confirmPreferenceButton.setOnClickListener {
            val NotificationTime = view.findViewById<EditText>(R.id.NotificationTime);
            startDate = if (startDate != null) startDate else DateVal(0, 0, 0);
            endDate = if (endDate != null) endDate else DateVal(0, 0, 0);
            startTime = if (startTime != null) startTime else TimeVal(0, 0);
            endTime = if (endTime != null) endTime else TimeVal(0, 0);
            generatePlanActivity.addPreference(
                PlanPreferenceDetail(
                    PreferenceName.text.toString(),
                    startDate!!,
                    endDate!!,
                    startTime!!,
                    endTime!!,
                    DurationVal(
                        NotificationTime.text.toString(),
                        timeUnitsSpinner.selectedItem.toString(),
                    )
                )
            );
            generatePlanActivity.showPlanInfoPage();
        }

    }
}
