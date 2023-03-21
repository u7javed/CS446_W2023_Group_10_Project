package cs446.group10.gen_s
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
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
    var preferenceName: String,
    var startDate: DateVal,
    var endDate: DateVal,
    var startTime: TimeVal,
    var endTime: TimeVal,
    var duration: DurationVal,
    var frequency: String,
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
        val preferenceDetail = this.preferenceDetails.preferenceDetail;

        val PreferenceName = view.findViewById<EditText>(R.id.PreferenceNameTextField);
        val chosenStartDate = view.findViewById<TextView>(R.id.chosenStartDate);
        val selectStartDateButton = view.findViewById<MaterialButton>(R.id.selectStartDate);
        val chosenEndDate = view.findViewById<TextView>(R.id.chosenEndDate);
        val selectEndDateButton = view.findViewById<MaterialButton>(R.id.selectEndDate);
        val chosenStartTime = view.findViewById<TextView>(R.id.chosenStartTime);
        val selectStartTimeButton = view.findViewById<MaterialButton>(R.id.selectStartTime);
        val chosenEndTime = view.findViewById<TextView>(R.id.chosenEndTime);
        val selectEndTimeButton = view.findViewById<MaterialButton>(R.id.selectEndTime);
        val timeUnitsSpinner = view.findViewById<Spinner>(R.id.DurationUnit);
        val durationTime = view.findViewById<EditText>(R.id.DurationTime);
        val frequency = view.findViewById<EditText>(R.id.Frequency);
        val timeUnits = resources.getStringArray(R.array.UnitsOfDuration);

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
            if (preferenceDetail != null) {
                PreferenceName.setText(preferenceDetail.preferenceName)
                startDate = preferenceDetail.startDate;
                chosenStartDate.text = DatePickerFragment.convertDateToString(startDate);
                endDate = preferenceDetail.endDate;
                chosenEndDate.text = DatePickerFragment.convertDateToString(endDate);
                startTime = preferenceDetail.startTime;
                chosenStartTime.text = TimePickerFragment.convertTimeToString(startTime);
                endTime = preferenceDetail.endTime;
                chosenEndTime.text = TimePickerFragment.convertTimeToString(endTime);
                durationTime.setText(preferenceDetail.duration.quantity);

                if (timeUnitsSpinner != null) {
                    val adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item, timeUnits
                        )
                    }
                    timeUnitsSpinner.adapter = adapter;
                    if (adapter != null) {
                        timeUnitsSpinner.setSelection(adapter.getPosition(preferenceDetail.duration.unit))
                    };
                }

                frequency.setText(preferenceDetail.frequency);
            };

        }

        // start date

        val startDateCalendarFragment = DatePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.StartDatePickerFragment, startDateCalendarFragment);
            commit();
        }

        fun setStartDate(dateVal: DateVal) {
            startDate = dateVal;
            chosenStartDate.text = DatePickerFragment.convertDateToString(dateVal);
        }

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
            endDate = dateVal;
            chosenEndDate.text = DatePickerFragment.convertDateToString(dateVal);
        }

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
            startTime = timeVal;
            chosenStartTime.text = TimePickerFragment.convertTimeToString(timeVal);
        }

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
            endTime = timeVal;
            chosenEndTime.text = TimePickerFragment.convertTimeToString(timeVal);
        }

        selectEndTimeButton.setOnClickListener {
            endTimeCalendarFragment.showTimePicker(
                TimePickerInitialVal(::setEndTime, endTime)
            );
        }

        if (timeUnitsSpinner != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item, timeUnits
                )
            }
            timeUnitsSpinner.adapter = adapter;
        }


        val cancelPreferenceButton = view.findViewById<MaterialButton>(R.id.CancelPreferenceButton);
        cancelPreferenceButton.setOnClickListener {
            generatePlanActivity.showPlanInfoPage();
        }

        val removePreferenceButton = view.findViewById<AppCompatImageButton>(R.id.removePreferenceButton);

        removePreferenceButton.visibility = if (preferenceDetails.newPreference) View.GONE else View.VISIBLE;

        removePreferenceButton.setOnClickListener {
            if (preferenceDetails.preferenceDetail != null) {
                generatePlanActivity.removePreference(preferenceDetails.preferenceDetail);
            }
            generatePlanActivity.showPlanInfoPage();
        }

        fun addPreference() {
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
                        durationTime.text.toString(),
                        timeUnitsSpinner.selectedItem.toString(),
                    ),
                    frequency.text.toString()
                )
            );
            generatePlanActivity.showPlanInfoPage();
        }

        fun updatePreference() {
            if (preferenceDetail != null) {
                startDate = if (startDate != null) startDate else DateVal(0, 0, 0);
                endDate = if (endDate != null) endDate else DateVal(0, 0, 0);
                startTime = if (startTime != null) startTime else TimeVal(0, 0);
                endTime = if (endTime != null) endTime else TimeVal(0, 0);
                preferenceDetail.preferenceName = PreferenceName.text.toString();
                preferenceDetail.startDate = startDate!!;
                preferenceDetail.endDate = endDate!!;
                preferenceDetail.startTime = startTime!!;
                preferenceDetail.endTime = endTime!!;
                preferenceDetail.duration = DurationVal(
                    durationTime.text.toString(),
                    timeUnitsSpinner.selectedItem.toString(),
                );
                preferenceDetail.frequency = frequency.text.toString();
            }
            generatePlanActivity.updatePreference();
            generatePlanActivity.showPlanInfoPage();
        }


        val confirmPreferenceButton = view.findViewById<MaterialButton>(R.id.ConfirmPreferenceButton);
        confirmPreferenceButton.setOnClickListener {
            if (newPreference) {
                addPreference();
            } else {
                updatePreference();
            }
        }
    }
}
