package com.example.charitymate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.charitymate.databinding.HungerReliefBinding
import com.google.firebase.firestore.FirebaseFirestore


class HungerRelief : AppCompatActivity() {

    private lateinit var binding: HungerReliefBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var mList = mutableListOf<HungerDetails>()
    private lateinit var adapter: HungerItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HungerReliefBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        getData()

        //Directing to other sections
        binding.btnHealth.setOnClickListener {
            val intent = Intent(this, Health::class.java)
            startActivity(intent)
        }

        binding.imageButton2.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        binding.btnEducation.setOnClickListener {
            val intent = Intent(this, Education::class.java)
            startActivity(intent)
        }

        binding.btnMF.setOnClickListener {
            val intent = Intent(this, Microfinance::class.java)
            startActivity(intent)
        }

    }

    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HungerItemsAdapter(mList)
        binding.recyclerView.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")

    private fun getData() {
        firebaseFirestore.collection("HungerDetails")
            .get()
            .addOnSuccessListener { documents ->
                mList.clear()
                for (document in documents) {
                    val data = document.data
                    val id = document.id
                    val title = data["title"].toString()
                    val description = data["description"].toString()
                    val location = data["location"].toString()
                    val amountNeeded = data["amountNeeded"].toString()
                    val contact = data["contact"].toString()
                    val startDate = data["startDate"].toString()
                    val endDate = data["endDate"].toString()
                    val imageUrl = data["pic"].toString()
                    val hungerDetails = HungerDetails(id, title, description, location, amountNeeded, contact, startDate, endDate, imageUrl)
                    mList.add(hungerDetails)
                }
                adapter.notifyDataSetChanged()
            }
    }
}