package cs446.group10.gen_s

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateFragment : Fragment(R.layout.fragment_generate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val generateEditPreferenceFragment = GenerateEditPreferenceFragment();

        val addPreferenceBtn = view.findViewById<ImageButton>(R.id.addPreferenceBtn);
        addPreferenceBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.GenerateMainFragment, generateEditPreferenceFragment);
                commit();
            }
        }
    }
}