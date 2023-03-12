package cs446.group10.gen_s.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.dataClasses.Event
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class EventListViewAdapter(
    private var dataSet: List<Event>,
    private val onClickListener: (eventId: String) -> Unit
) :
    RecyclerView.Adapter<EventListViewAdapter.ViewHolder>() {

    val months = listOf("Holder", "Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val primaryItem: LinearLayout
        val dates: TextView
        val times: TextView
        val name: TextView
        val plan: TextView
        val notification: TextView

        init {
            primaryItem = view.findViewById(R.id.eventViewPrimaryItem)
            dates = view.findViewById(R.id.eventViewDates)
            times = view.findViewById(R.id.eventViewTimes)
            name = view.findViewById(R.id.eventViewName)
            plan = view.findViewById(R.id.eventViewPlan)
            notification = view.findViewById(R.id.eventViewNotification)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event: Event = dataSet[position]

        // Assign Date
        val startDate: LocalDateTime = LocalDateTime.ofEpochSecond(event.startDate, 0, ZoneOffset.UTC)
        val endDate: LocalDateTime = LocalDateTime.ofEpochSecond(event.endDate, 0, ZoneOffset.UTC)

        val startDay: String = startDate.dayOfWeek.toString().lowercase().replaceFirstChar { it.titlecase() }

        var dateText = "$startDay - ${months[startDate.monthValue]} ${startDate.dayOfMonth}"
        if (startDate.dayOfWeek != endDate.dayOfWeek) {
            dateText += " to ${months[endDate.monthValue]} ${endDate.dayOfMonth}"
        }

        holder.dates.text = dateText
        holder.name.text = event.name
        holder.plan.text = "Plan: None" // TODO: add plan data

        val startTimeStr = timeFormatter.format(startDate.toLocalTime()).toString()
        val endTimeStr = timeFormatter.format(endDate.toLocalTime()).toString()
        holder.times.text = "${startTimeStr}\n - \n${endTimeStr}"

        var notificationStr = "None"
        if (event.notification != null) {
            val notification: LocalDateTime = LocalDateTime.ofEpochSecond(event.notification!!, 0, ZoneOffset.UTC)
            val notiDay: String = notification.dayOfMonth.toString()
            val notiMonth: String = months[notification.monthValue]
            val notiTime: String = timeFormatter.format(notification.toLocalTime()).toString()
            notificationStr = "$notiDay $notiMonth $notiTime"
        }
        holder.notification.text = "Notification: $notificationStr"
        holder.primaryItem.setOnClickListener {
            onClickListener(event.eventId)
        }
    }


    override fun getItemCount() = dataSet.size

}