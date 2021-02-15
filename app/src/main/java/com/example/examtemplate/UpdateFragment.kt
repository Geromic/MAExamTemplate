package com.example.examtemplate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import data.Item
import network.RetrofitClientImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import viewmodel.ItemViewModel


class UpdateFragment(private val item: Item) : Fragment() {

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        val listFragment = ListFragment()

        println(item)
        val editName: EditText = view.findViewById(R.id.updateNameField)
        editName.setText(item.name)
        val editLevel: EditText = view.findViewById(R.id.updateLevelField)
        editLevel.setText(item.level.toString())
        val editStatus: EditText = view.findViewById(R.id.updateStatusField)
        editStatus.setText(item.status)
        val editFrom: EditText = view.findViewById(R.id.updateFromField)
        editFrom.setText(item.from.toString())
        val editTo: EditText = view.findViewById(R.id.updateToField)
        editTo.setText(item.to.toString())

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        val updateBtn: Button = view.findViewById(R.id.updateButton)
        updateBtn.setOnClickListener {
            item.name = editName.text.toString()
            item.level = editLevel.text.toString().toInt()
            item.status = editStatus.text.toString()
            item.from = editFrom.text.toString().toInt()
            item.to = editTo.text.toString().toInt()

            updateItem(item)

            parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, listFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
        }

        val cancelUpdateBtn: Button = view.findViewById(R.id.cancelUpdateButton)
        cancelUpdateBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, listFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
        }

        return view
    }

    private fun updateItem(item: Item) {
        RetrofitClientImpl.retrofitClientInstance?.updateItem(item)?.enqueue(
                object : Callback<Item> {
                    override fun onResponse(call: Call<Item>, response: Response<Item>) {
                        println("Update successful")
                    }

                    override fun onFailure(call: Call<Item>, t: Throwable) {
                        Toast.makeText(context, "Unable to reach server", Toast.LENGTH_LONG).show()
                    }
                }
        )
    }
}