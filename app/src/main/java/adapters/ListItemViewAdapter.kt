package adapters

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.examtemplate.R
import com.example.examtemplate.UpdateFragment
import data.Item
import network.RetrofitClient
import network.RetrofitClientImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import viewmodel.ItemViewModel
import java.util.*

class ListItemViewAdapter(context: Context, fragmentManager: FragmentManager) : RecyclerView.Adapter<ListItemViewAdapter.ViewHolder>() {
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
        holder.itemName.text = items.get(position).id.toString() + ". " + items.get(position).name + " -- " + items.get(position).level.toString()
        holder.updateBtn.setOnClickListener { v: View? ->
            updateItem(context, items.get(position).id)
        }
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
        val updateBtn: Button
        val deleteBtn: Button

        init {
            itemName = itemView.findViewById(R.id.itemName)
            updateBtn = itemView.findViewById(R.id.updateBtn)
            deleteBtn = itemView.findViewById(R.id.deleteBtn)
        }
    }

    init {
        itemViewModel = ItemViewModel(context.applicationContext as Application)
        this.context = context
        this.fragmentManager = fragmentManager

        //fetchData(context)
    }

    private fun fetchData(context: Context) {
        RetrofitClientImpl.retrofitClientInstance?.getAllItems()?.enqueue(
                object : Callback<List<Item>> {
                    override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                        setItems(response.body()!!)
                    }

                    override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                        Toast.makeText(context, "Unable to reach server", Toast.LENGTH_LONG).show()
                    }
                }
        )
    }

    private fun updateItem(context: Context, id: Int) {
        RetrofitClientImpl.retrofitClientInstance?.getItemById(id)?.enqueue(
                object : Callback<Item> {
                    override fun onResponse(call: Call<Item>, response: Response<Item>) {
                        val updateFragment = response.body()?.let { UpdateFragment(it) };
                        if (updateFragment != null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, updateFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit()
                        } else {
                            println("Empty response")
                        }
                    }

                    override fun onFailure(call: Call<Item>, t: Throwable) {
                        Toast.makeText(context, "Unable to reach server", Toast.LENGTH_LONG).show()
                    }
                }
        )
    }
}