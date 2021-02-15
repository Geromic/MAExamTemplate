package com.example.examtemplate

import adapters.ListItemViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import data.Item
import data.ItemDatabase
import kotlinx.android.synthetic.main.fragment_list.view.*
import network.RetrofitClientImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import viewmodel.ItemViewModel
import java.util.*
import java.util.function.ToIntFunction
import java.util.stream.Collectors

class ListFragment : Fragment() {
    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        val layoutManager = LinearLayoutManager(activity)
        val adapter = ListItemViewAdapter(activity!!, parentFragmentManager)
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager


        itemViewModel.data.observe(viewLifecycleOwner, Observer { items ->
            RetrofitClientImpl.retrofitClientInstance?.getAllItems()?.enqueue(object :
                Callback<List<Item>> {
                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                    val items: List<Item> = response.body()!!
                    adapter.setItems(items)
                }

                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    itemViewModel.data.observe(viewLifecycleOwner, Observer {
                            shows -> adapter.setItems(shows)
                    })

                    Toast.makeText(
                        context,
                        "Unable to reach server",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        })

        return view
    }

}