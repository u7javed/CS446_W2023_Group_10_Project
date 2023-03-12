package cs446.group10.gen_s
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import java.util.*

data class PlanPreferenceDetail(
    val preferenceName: String,
    val startDate: Date,
    val endDate: Date,
    val startTime: Int,
    val endTime: Int,
    val duration: Int,
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
class GenerateEditPreferenceFragment(private val preferenceDetails: PlanPreferenceInitialVal) :
    Fragment(R.layout.fragment_generate_edit_preference) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val newPreference = this.preferenceDetails.newPreference;

        val preferenceName = view.findViewById<EditText>(R.id.PreferenceNameTextField);

        var startDate: DateVal? = null;
        var endDate: DateVal? = null;

        if (newPreference) {
            // will be removed later
            // used to test passing parameters from one fragment (GenerateFragment) to another fragment (GenerateEditPreferenceFragment)
            preferenceName.setText("New Preference");
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
    }
}
