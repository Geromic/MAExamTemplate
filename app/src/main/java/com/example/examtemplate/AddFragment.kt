package com.example.examtemplate

import android.content.ClipData
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


class AddFragment : Fragment() {

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        val addButton: Button = view.findViewById(R.id.addButton)

        val nameEdit: EditText = view.findViewById(R.id.nameInput)
        val levelEdit: EditText = view.findViewById(R.id.levelInput)
        val statusEdit: EditText = view.findViewById(R.id.statusInput)
        val fromEdit: EditText = view.findViewById(R.id.fromInput)
        val toEdit: EditText = view.findViewById(R.id.toInput)

        addButton.setOnClickListener {
            if (nameEdit.text.toString() != "" ||
                    levelEdit.text.toString() != "" ||
                    statusEdit.text.toString() != "" ||
                    fromEdit.text.toString() != "" ||
                    toEdit.text.toString() != "") {
                val toAdd = Item(-1,
                        nameEdit.text.toString(),
                        levelEdit.text.toString().toInt(),
                        statusEdit.text.toString(),
                        fromEdit.text.toString().toInt(),
                        toEdit.text.toString().toInt()
                )

                addItem(toAdd)

                val listFragment = ListFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, listFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun addItem(item: Item) {
        RetrofitClientImpl.retrofitClientInstance?.saveItem(item)?.enqueue(
            object : Callback<Item> {
                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    println("Add successful")
                }

                override fun onFailure(call: Call<Item>, t: Throwable) {
                    Toast.makeText(context, "Unable to reach server", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}