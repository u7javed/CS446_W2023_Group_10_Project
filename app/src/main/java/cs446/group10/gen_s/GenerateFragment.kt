package cs446.group10.gen_s

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import cs446.group10.gen_s.ui.activities.GeneratePlanActivity

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateFragment(
    private val generatePlanActivity: GeneratePlanActivity,
    private val preferenceItemsAdapter: ListTabsAdapter,
) : Fragment(R.layout.fragment_generate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceItemsAdapter.updateList(generatePlanActivity.getPreferences());
        val rvPreferences = view.findViewById<RecyclerView>(R.id.RVPreferences);

        rvPreferences.adapter = preferenceItemsAdapter;
        rvPreferences.layoutManager = LinearLayoutManager(context);

        // set up add preference button to show edit preference fragment
        val addPreferenceBtn = view.findViewById<ImageButton>(R.id.addPreferenceBtn);
        addPreferenceBtn.setOnClickListener {
            generatePlanActivity.showEditPreferencePage();
        }

    }
}