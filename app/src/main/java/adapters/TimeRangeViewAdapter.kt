package adapters

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examtemplate.R
import data.Item
import viewmodel.ItemViewModel
import java.util.ArrayList

class TimeRangeViewAdapter(context: Context, fragmentManager: FragmentManager) : RecyclerView.Adapter<TimeRangeViewAdapter.ViewHolder>() {
    private val itemViewModel: ItemViewModel
    private var items: List<Item> = ArrayList<Item>()
    private val context: Context
    private val fragmentManager: FragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.setText(items.get(position).id.toString() + ". " + items.get(position).name + " -- " + items.get(position).level.toString())
        holder.itemRange.setText("Range: " + items.get(position).from.toString() + " -- " + items.get(position).to.toString())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView
        val itemRange: TextView

        init {
            itemName = itemView.findViewById(R.id.simpleField)
            itemRange = itemView.findViewById(R.id.rangeField)
        }
    }

    init {
        itemViewModel = ItemViewModel(context.applicationContext as Application)
        this.context = context
        this.fragmentManager = fragmentManager
    }
}