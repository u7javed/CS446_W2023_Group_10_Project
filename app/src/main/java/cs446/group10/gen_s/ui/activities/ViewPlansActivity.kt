package cs446.group10.gen_s.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.dataClasses.Event
import cs446.group10.gen_s.backend.dataClasses.Plan
import cs446.group10.gen_s.backend.model.IView
import cs446.group10.gen_s.backend.view_model.ViewModel
import cs446.group10.gen_s.ui.adapters.EventListViewAdapter
import cs446.group10.gen_s.ui.adapters.PlanListViewAdapter

class ViewPlansActivity : AppCompatActivity(), IView {

    private val _viewModel = ViewModel
    private lateinit var _plans: List<Plan>
    private lateinit var _recyclerView: RecyclerView
    private lateinit var _recyclerAdapter: PlanListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your Plans"

        // Update events
        _plans = _viewModel.getAllPlans()

        // Register this view to the model
        _viewModel.registerView(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        _recyclerView = findViewById(R.id.eventsListView)
        _recyclerAdapter = PlanListViewAdapter(
            _plans,
            onClickListener = ::moveToEditPlanScreen
        )
        _recyclerView.layoutManager = LinearLayoutManager(this)
        _recyclerView.adapter = _recyclerAdapter
        _recyclerView.setHasFixedSize(false)
    }

    private fun moveToEditPlanScreen(planId: String) {
        val editPlanIntent = Intent(this, EditPlanActivity::class.java)
        editPlanIntent.putExtra("planId", planId)
        editPlanIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(editPlanIntent)
    }

    override fun update() {
        _plans = _viewModel.getAllPlans()
        _recyclerAdapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}