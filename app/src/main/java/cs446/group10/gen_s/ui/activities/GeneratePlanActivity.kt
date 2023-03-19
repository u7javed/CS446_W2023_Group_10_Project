package cs446.group10.gen_s.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import cs446.group10.gen_s.*

class GeneratePlanActivity : AppCompatActivity() {
    private var preferences = mutableListOf<PlanPreferenceDetail>();
    private var preferenceItems = mutableListOf<ListTabDetail>();
//    private var preferenceItems = mutableListOf<ListTabDetail>(
//    ListTabDetail(
//        "HI",
//        "D1",
//        "D2",
//        imageResourceId=R.drawable.editicon,
//    ),
//    ListTabDetail(
//        "HI",
//        "D1",
//        "D2",
//        imageResourceId=R.drawable.editicon,
//    ),
//    ListTabDetail(
//        "HI",
//        "D1",
//        "D2",
//        imageResourceId=R.drawable.editicon,
//    ),
//    ListTabDetail(
//        "HI",
//        "D1",
//        "D2",
//        imageResourceId=R.drawable.editicon,
//    ),
//    ListTabDetail(
//        "HI",
//        "D1",
//        "D2",
//        imageResourceId=R.drawable.editicon,
//    ),
//    ListTabDetail(
//        "HI",
//        "D1",
//        "D2",
//        imageResourceId=R.drawable.editicon,
//    ),
//    );
    private lateinit var preferenceItemsAdapter: ListTabsAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_plan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Generate Plan"

        preferenceItemsAdapter = ListTabsAdapter(preferenceItems);
        val generateFragment = GenerateFragment(
            this,
            preferenceItemsAdapter
        );

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.GenerateMainFragment, generateFragment);
            commit();
        }
    }

    public fun getPreferences(): MutableList<ListTabDetail> {
        return preferenceItems;
    }

    public fun showPlanInfoPage() {
        val generateFragment = GenerateFragment(
            this,
            preferenceItemsAdapter
        );
        supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.GenerateMainFragment, generateFragment);
            commit();
        }
    }

    public fun showEditPreferencePage() {
        val generateEditPreferenceFragment = GenerateEditPreferenceFragment(
            PlanPreferenceInitialVal(true, null),
            this
        );

        supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.GenerateMainFragment, generateEditPreferenceFragment);
            commit();
        }
    }

    public fun addPreference(newPreference: PlanPreferenceDetail) {
        preferences.add(newPreference);
        preferenceItems.add(
            ListTabDetail(
                newPreference.preferenceName,
                DatePickerFragment.convertDateToString(newPreference.startDate) +
                        " - " +
                        DatePickerFragment.convertDateToString(newPreference.endDate) +
                        " | " +
                        TimePickerFragment.convertTimeToString(newPreference.startTime) +
                        " - " +
                        TimePickerFragment.convertTimeToString(newPreference.endTime),
                newPreference.duration.quantity + " " + newPreference.duration.unit +
                        " | " +
                        "Every " + newPreference.frequency + " day(s)",
                imageResourceId=R.drawable.editicon,
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}