package cs446.group10.gen_s
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
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

        if (newPreference) {
            // will be removed later
            // used to test passing parameters from one fragment (GenerateFragment) to another fragment (GenerateEditPreferenceFragment)
            preferenceName.setText("New Plan");
        }
    }
}