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
import com.google.gson.Gson
import cs446.group10.gen_s.R

class ViewPlansActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_plans)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your Plans"

        val viewPlansLayout = findViewById<LinearLayout>(R.id.view_plans)

        val plans : ArrayList<ViewPlansActivity.Plan> = arrayListOf<ViewPlansActivity.Plan>(
            ViewPlansActivity.Plan("Plan 1", "Jan 3", "Jan 12", arrayListOf<ViewEventsActivity.Event>(ViewEventsActivity.Event("Event 2", "Jan 4", "11am", "12pm"))),
            ViewPlansActivity.Plan("Event 2", "Jan 4", "Jan 5", arrayListOf<ViewEventsActivity.Event>())
        )

        for (plan in plans) {
            // eventLayout
            val planLayout = LinearLayout(this)
            planLayout.orientation = LinearLayout.HORIZONTAL
            planLayout.gravity = Gravity.CENTER_VERTICAL
            planLayout.setPadding(
                dpToPixel(16),
                dpToPixel(6),
                dpToPixel(16),
                dpToPixel(6)
            )

            val planLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            planLayoutParams.setMargins(
                dpToPixel(8),
                dpToPixel(12),
                dpToPixel(8),
                0
            )

            planLayout.layoutParams = planLayoutParams

            // infoLayout (left)
            val infoLayout = LinearLayout(this)
            infoLayout.orientation = LinearLayout.VERTICAL
            infoLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )

            // eventName (top left)
            val planNameTextView = TextView(this)
            planNameTextView.text = plan.planName
            planNameTextView.textSize = 16f
            planNameTextView.setTextColor(Color.BLACK)

            // dates (middle left)
            val dateTextView = TextView(this)
            dateTextView.text = "${plan.startdate} - ${plan.enddate}"
            dateTextView.textSize = 12f
            dateTextView.setTextColor(Color.parseColor("#DD000000"))

            // numberOfEvents (bottom left)
            val eventsTextView = TextView(this)
            eventsTextView.text = "${plan.events.size} event(s)"
            eventsTextView.textSize = 12f
            eventsTextView.setTextColor(Color.parseColor("#DD000000"))

            // infoLayout (left) has 3 children
            infoLayout.addView(planNameTextView)
            infoLayout.addView(dateTextView)
            infoLayout.addView(eventsTextView)

            //TO DO: Fix spacing
            // edit image (right)
            val editImageView = ImageButton(this)
            editImageView.setImageResource(R.drawable.editicon)
            editImageView.layoutParams = LinearLayout.LayoutParams(
                dpToPixel(24),
                dpToPixel(24)
            )
            editImageView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    moveToEditPlanPage(plan.planName, plan.startdate, plan.enddate, plan.events)
                }
            })
            // eventLayout has 2 children (left, right)
            planLayout.addView(infoLayout)
            planLayout.addView(editImageView)

            viewPlansLayout.addView(planLayout)
        }
    }

    private fun dpToPixel(_dp: Int) : Int {
        return (_dp * (resources.displayMetrics.density)).toInt()
    }

    data class Plan(
        val planName: String,
        val startdate: String,
        val enddate: String,
        val events: ArrayList<ViewEventsActivity.Event>
    )

    private fun moveToEditPlanPage(planName: String, startDate: String, endDate: String, events: ArrayList<ViewEventsActivity.Event>): Boolean {
        val editPlanIntent = Intent(this, EditPlanActivity::class.java)
        val eventsAsString: String = Gson().toJson(events)
        editPlanIntent.putExtra("planName", planName)
        editPlanIntent.putExtra("startDate", startDate)
        editPlanIntent.putExtra("endDate", endDate)
        editPlanIntent.putExtra("events", eventsAsString)
        editPlanIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(editPlanIntent)
        return true
    }
}