package cs446.group10.gen_s.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.dataClasses.Plan
import java.time.LocalDateTime
import java.time.ZoneOffset

class PlanListViewAdapter(
    private var dataSet: List<Plan>,
    private val onClickListener: (planId: String) -> Unit
) :
    RecyclerView.Adapter<PlanListViewAdapter.ViewHolder>() {

    val months = listOf("Holder", "Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val primaryItem: LinearLayout
        val dates: TextView
        val name: TextView
        val numEvents: TextView

        init {
            primaryItem = view.findViewById(R.id.planViewPrimaryItem)
            dates = view.findViewById(R.id.planViewDates)
            name = view.findViewById(R.id.planViewName)
            numEvents = view.findViewById(R.id.planViewNumEvents)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan: Plan = dataSet[position]

        // Assign Date
        val startDate: LocalDateTime = LocalDateTime.ofEpochSecond(plan.start, 0, ZoneOffset.UTC)
        val endDate: LocalDateTime = LocalDateTime.ofEpochSecond(plan.end, 0, ZoneOffset.UTC)

        val startDay: String = startDate.dayOfWeek.toString().lowercase().replaceFirstChar { it.titlecase() }

        var dateText = "$startDay - ${months[startDate.monthValue]} ${startDate.dayOfMonth}"
        if (startDate.dayOfWeek != endDate.dayOfWeek) {
            dateText += " to ${months[endDate.monthValue]} ${endDate.dayOfMonth}"
        }

        holder.dates.text = dateText
        holder.name.text = plan.name

        holder.primaryItem.setBackgroundColor(Color.parseColor(plan.color))
        holder.numEvents.text = plan.events.size.toString()

        holder.primaryItem.setOnClickListener {
            onClickListener(plan.planId)
        }
    }


    override fun getItemCount() = dataSet.size

}