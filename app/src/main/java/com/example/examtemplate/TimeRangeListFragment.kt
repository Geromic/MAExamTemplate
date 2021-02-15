package com.example.examtemplate

import adapters.LevelFilterViewAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import data.Item
import kotlinx.android.synthetic.main.fragment_level_filter_list.view.*
import kotlinx.android.synthetic.main.fragment_time_range_list.view.*
import network.RetrofitClientImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import viewmodel.ItemViewModel

class TimeRangeListFragment : Fragment() {
    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_time_range_list, container, false)

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        val layoutManager = LinearLayoutManager(activity)
        val adapter = LevelFilterViewAdapter(activity!!, parentFragmentManager)
        val recyclerView = view.timeRangeFilterRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        val filterBtn: Button = view.findViewById(R.id.timeRangeFilterButton)
        filterBtn.setOnClickListener {
            val from = view.findViewById<EditText>(R.id.fromFilterInput).text.toString().toInt()
            val to = view.findViewById<EditText>(R.id.toFilterInput).text.toString().toInt()
            itemViewModel.data.observe(viewLifecycleOwner, Observer { items ->
                RetrofitClientImpl.retrofitClientInstance?.getAllItems()?.enqueue(object :
                    Callback<List<Item>> {
                    override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                        var items: List<Item> = response.body()!!

                        items = items.filter { item -> item.from >= from && item.to <= to }

                        adapter.setItems(items)
                    }

                    override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                        /*itemViewModel.data.observe(viewLifecycleOwner, Observer {
                                shows -> adapter.setItems(shows)
                        })*/

                        Toast.makeText(
                            context,
                            "Unable to reach server",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            })
        }

        return view
    }
}