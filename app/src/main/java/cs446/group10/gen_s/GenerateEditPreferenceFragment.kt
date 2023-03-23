package cs446.group10.gen_s
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
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

        fun addPreference(preferenceData: PlanPreferenceDetail) {
            generatePlanActivity.addPreference(preferenceData);
            generatePlanActivity.showPlanInfoPage();
        }

        fun updatePreference(preferenceData: PlanPreferenceDetail) {
            if (preferenceDetail != null) {
                preferenceDetail.preferenceName = preferenceData.preferenceName;
                preferenceDetail.startDate = preferenceData.startDate;
                preferenceDetail.endDate = preferenceData.endDate;
                preferenceDetail.startTime = preferenceData.startTime;
                preferenceDetail.endTime = preferenceData.endTime;
                preferenceDetail.duration = DurationVal(
                    preferenceData.duration.quantity,
                    preferenceData.duration.unit,
                );
            }
            generatePlanActivity.updatePreference();
            generatePlanActivity.showPlanInfoPage();
        }


        val confirmPreferenceButton = view.findViewById<MaterialButton>(R.id.ConfirmPreferenceButton);
        confirmPreferenceButton.setOnClickListener {
            if (PreferenceName.text.toString() == "" ||
                startDate == null || endDate == null || startTime == null || endTime == null ||
                durationTime.text.toString() == "") {
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle("Invalid Input")
                builder?.setMessage("Fill in all the information to proceed.")
                builder?.setPositiveButton("Confirm") { dialog, _ ->
                    dialog.cancel()
                }
                val alert = builder?.create()
                alert?.show()
                return@setOnClickListener;
            }

            val preferenceData = PlanPreferenceDetail(
                PreferenceName.text.toString(),
                startDate!!,
                endDate!!,
                startTime!!,
                endTime!!,
                DurationVal(
                    durationTime.text.toString(),
                    timeUnitsSpinner.selectedItem.toString(),
                ),
            )
            if (newPreference) {
                addPreference(preferenceData);
            } else {
                updatePreference(preferenceData);
            }
        }
    }
}
