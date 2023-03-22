package cs446.group10.gen_s.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import cs446.group10.gen_s.*
import cs446.group10.gen_s.backend.dataClasses.Preference
import cs446.group10.gen_s.backend.view_model.ViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset

class GeneratePlanActivity : AppCompatActivity() {
    private var preferences = mutableListOf<PlanPreferenceDetail>();
    private var preferenceItems = mutableListOf<ListTabDetail>();
    private lateinit var generateFragment: GenerateFragment;
    private lateinit var preferenceItemsAdapter: ListTabsAdapter;
    private var planName: String = "";
    private var startDate: DateVal? = null;
    private var endDate: DateVal? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_plan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Generate Plan"

        val planBasicInfo = PlanBasicInfoFragment.formPlanBasicInfo(
            planName,
            startDate,
            endDate
        )

        preferenceItemsAdapter = ListTabsAdapter(preferenceItems);
        generateFragment = GenerateFragment(
            this,
            preferenceItemsAdapter,
            planBasicInfo
        );

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.GenerateMainFragment, generateFragment);
            commit();
        }
    }

    private fun mapToViewModelPreference(): MutableList<Preference> {
        val result = mutableListOf<Preference>();
        for (preference in preferences) {
            result.add(
                Preference(
                    preference.preferenceName,
                    LocalDateTime.of(
                        preference.startDate.year,
                        preference.startDate.month + 1,
                        preference.startDate.day,
                        preference.startTime.hour,
                        preference.startTime.minute
                    ).toEpochSecond(ZoneOffset.UTC),
                    LocalDateTime.of(
                        preference.endDate.year,
                        preference.endDate.month + 1,
                        preference.endDate.day,
                        preference.endTime.hour,
                        preference.endTime.minute
                    ).toEpochSecond(ZoneOffset.UTC),
                    (if (preference.duration.unit == "Hour(s)")
                        preference.duration.quantity.toInt() * 60 * 60
                    else preference.duration.quantity.toInt() * 60).toLong()
                )
            )
        }
        return result;
    }

    public fun generatePlan() {
        val planBasicInfo = generateFragment.getPlanBasicInfo();
        if (!planBasicInfo.valid) {
            val builder = this.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle("Invalid Input")
            builder?.setMessage("Fill in all the information to proceed.")
            builder?.setPositiveButton("Confirm") { dialog, _ ->
                dialog.cancel()
            }
            val alert = builder?.create()
            alert?.show()
            return;
        }

        ViewModel.addPlanToCalendar(
            planBasicInfo.planName,
            mapToViewModelPreference(),
            LocalDateTime.of(
                planBasicInfo.startDate!!.year,
                planBasicInfo.startDate!!.month + 1,
                planBasicInfo.startDate!!.day,
                0,
                0
            ).toEpochSecond(ZoneOffset.UTC),
            LocalDateTime.of(
                planBasicInfo.endDate!!.year,
                planBasicInfo.endDate!!.month + 1,
                planBasicInfo.endDate!!.day,
                23,
                59
            ).toEpochSecond(ZoneOffset.UTC),
        )
    }

    public fun getPreferences(): MutableList<PlanPreferenceDetail> {
        return preferences;
    }

    public fun getPreferencesTabs(): MutableList<ListTabDetail> {
        return preferenceItems;
    }

    public fun showPlanInfoPage() {
        val planBasicInfo = PlanBasicInfoFragment.formPlanBasicInfo(
            planName,
            startDate,
            endDate
        )

        generateFragment = GenerateFragment(
            this,
            preferenceItemsAdapter,
            planBasicInfo,
        );
        supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.GenerateMainFragment, generateFragment);
            commit();
        }
    }

    public fun showEditPreferencePage(preferenceDetail: PlanPreferenceDetail? = null) {
        val planBasicInfo = generateFragment.getPlanBasicInfo();
        planName = planBasicInfo.planName;
        startDate = planBasicInfo.startDate;
        endDate = planBasicInfo.endDate;

        val generateEditPreferenceFragment = GenerateEditPreferenceFragment(
            PlanPreferenceInitialVal(preferenceDetail == null, preferenceDetail),
            this
        );

        supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.GenerateMainFragment, generateEditPreferenceFragment);
            commit();
        }
    }

    public fun formListTabDetail(preference: PlanPreferenceDetail): ListTabDetail {
        fun editPreference() {
            showEditPreferencePage(preference);
        }
        return ListTabDetail(
            preference.preferenceName,
            DatePickerFragment.convertDateToString(preference.startDate) +
                    " - " +
                    DatePickerFragment.convertDateToString(preference.endDate) +
                    " | " +
                    TimePickerFragment.convertTimeToString(preference.startTime) +
                    " - " +
                    TimePickerFragment.convertTimeToString(preference.endTime),
            preference.duration.quantity + " " + preference.duration.unit +
                    " | " +
                    "Every " + preference.frequency + " day(s)",
            imageResourceId=R.drawable.editicon,
            onClick=::editPreference
        )
    }

    public fun addPreference(newPreference: PlanPreferenceDetail) {
        preferences.add(newPreference);
        preferenceItems.add(formListTabDetail(newPreference));
    }

    private fun setPreferenceItemsUsingPreference() {
        preferenceItems = mutableListOf<ListTabDetail>();
        for (preference in preferences) {
            preferenceItems.add(formListTabDetail(preference));
        }
    }

    public fun updatePreference() {
        setPreferenceItemsUsingPreference();
    }

    public fun removePreference(preferenceToRemove: PlanPreferenceDetail) {
        preferences.remove(preferenceToRemove);
        setPreferenceItemsUsingPreference();
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}