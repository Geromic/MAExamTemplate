package com.example.examtemplate

import adapters.LevelFilterViewAdapter
import adapters.ListItemViewAdapter
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
import kotlinx.android.synthetic.main.fragment_list.view.*
import network.RetrofitClientImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import viewmodel.ItemViewModel

class LevelFilterListFragment : Fragment() {
    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_level_filter_list, container, false)

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        val layoutManager = LinearLayoutManager(activity)
        val adapter = LevelFilterViewAdapter(activity!!, parentFragmentManager)
        val recyclerView = view.levelFilterRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        val filterBtn: Button = view.findViewById(R.id.levelFilterButton)
        filterBtn.setOnClickListener {
            val level = view.findViewById<EditText>(R.id.levelFilterInput).text.toString().toInt()
            itemViewModel.data.observe(viewLifecycleOwner, Observer { items ->
                RetrofitClientImpl.retrofitClientInstance?.getAllByLevel(level)?.enqueue(object :
                    Callback<List<Item>> {
                    override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                        val items: List<Item> = response.body()!!

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