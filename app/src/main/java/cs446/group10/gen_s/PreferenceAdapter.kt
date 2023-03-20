package cs446.group10.gen_s

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

data class ListTabDetail(
    var header: String? = null,
    var description1: String? = null,
    var description2: String? = null,
    var imageResourceId: Int? = null, // R.drawable.xxxxx
)

class ListTabsAdapter(
    private var listTabs: MutableList<ListTabDetail>,
    private var uniqueItemCount: Int = 0
) : RecyclerView.Adapter<ListTabsAdapter.ListTabsViewHolder>() {
    class ListTabsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tabHeader: TextView;
        val tabDescription1: TextView;
        val tabDescription2: TextView;
        val tabIcon: ImageButton;

        init {
            tabHeader = itemView.findViewById<TextView>(R.id.itemHeader);
            tabDescription1 = itemView.findViewById<TextView>(R.id.itemDescription1);
            tabDescription2 = itemView.findViewById<TextView>(R.id.itemDescription2);
            tabIcon = itemView.findViewById<ImageButton>(R.id.itemIcon);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTabsViewHolder {
        return ListTabsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_tab,
                parent,
                false
            )
        )
    }

    public fun updateList(newListTabs: MutableList<ListTabDetail>) {
        listTabs = newListTabs;
        notifyDataSetChanged();
    }

    private fun updateTabValues(item: ListTabDetail, viewHolder: ListTabsViewHolder) {
        if (item.header != null) {
            viewHolder.tabHeader.visibility = View.VISIBLE;
            viewHolder.tabHeader.text = item.header;
        } else {
            viewHolder.tabHeader.visibility = View.GONE;
        }
        if (item.description1 != null) {
            viewHolder.tabDescription1.visibility = View.VISIBLE;
            viewHolder.tabDescription1.text = item.description1;
        } else {
            viewHolder.tabDescription1.visibility = View.GONE;
        }
        if (item.description2 != null) {
            viewHolder.tabDescription2.visibility = View.VISIBLE;
            viewHolder.tabDescription2.text = item.description2;
        } else {
            viewHolder.tabDescription2.visibility = View.GONE;
        }
        if (item.description2 != null) {
            viewHolder.tabDescription2.visibility = View.VISIBLE;
            viewHolder.tabDescription2.text = item.description2;
        } else {
            viewHolder.tabDescription2.visibility = View.GONE;
        }
        if (item.imageResourceId != null) {
            viewHolder.tabIcon.visibility = View.VISIBLE;
            viewHolder.tabIcon.setImageResource(item.imageResourceId!!);
        } else {
            viewHolder.tabIcon.visibility = View.GONE;
        }
    }

    override fun onBindViewHolder(viewHolder: ListTabsViewHolder, position: Int) {
        val curItem = listTabs[position];
        updateTabValues(curItem, viewHolder);

    }

    override fun getItemCount(): Int {
        return listTabs.size;
    }
}