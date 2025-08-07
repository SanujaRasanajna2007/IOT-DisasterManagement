package com.example.mad_day3.Controller

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_day3.Model.landslideTilt
import com.example.mad_day3.R
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

class loadCardsController {
    val db = Firebase.firestore
    val database = FirebaseDatabase.getInstance()
    val landslideMovementRef: DatabaseReference = database.getReference("Sensors/MPU6050Readings")
    val landslideTiltRef: DatabaseReference = database.getReference("Sensors/TiltReadings")
    private var tiltEventListener: ValueEventListener? = null
    public fun getTiltData(view: View, cityName: String?) {
        // Remove existing listener if any
        tiltEventListener?.let { landslideTiltRef.removeEventListener(it) }

        tiltEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var latestReading: landslideTilt? = null
                    for (childSnapshot in snapshot.children) {
                        val reading = childSnapshot.getValue(landslideTilt::class.java)
                        reading?.let {
                            // Check if timestamp exists before comparing
                            if (it.timestamp != null) {
                                if (latestReading == null ||
                                    (latestReading?.timestamp != null &&
                                            it.timestamp > latestReading!!.timestamp)) {
                                    latestReading = it
                                }
                            }
                        }
                    }
                    latestReading?.let {
                        if(it.location == cityName){
                            view.findViewById<TextView>(R.id.tiltStatus)?.text =
                                it.value?.toString() ?: "N/A"
                        }
//                        view.findViewById<TextView>(R.id.tiltTitleID)?.text =
//                            it.timestamp.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Data read failed: ${error.message}")
            }
        }
        tiltEventListener?.let { landslideTiltRef.addValueEventListener(it) }
    }

    private fun setupRecyclerView(view: View, items: List<LandSlideItem>, context: Context) {
        val landSlideRecyclerView = view.findViewById<RecyclerView>(R.id.recycleview)
        landSlideRecyclerView.layoutManager = LinearLayoutManager(context)
        landSlideRecyclerView.adapter = landSlideAdapter(items)

        // Add this to verify if adapter is set
        Log.d("AlertsFragment", "RecyclerView adapter set with ${items.size} items")
    }
    public fun getLandslideCard(view: View, savedInstanceState: Bundle?, context: Context, cityName: String?){
        try {
            db.collection("locationInfo")
                .whereEqualTo("name", cityName)
                .get()
                .addOnSuccessListener { result ->
                    Log.d("AlertsFragment", "Found ${result.size()} documents")
                    val landslideItems = mutableListOf<LandSlideItem>()
                    for (document in result) {
                        when (document.getString("category")) {
                            "Landslide" -> {
//                                val moisture = document.getDouble("moisture") ?: 0.0
//                                val tilt = document.getString("tilt") ?: "Unknown"
//                                val movement = document.getString("movement") ?: "Unknown"
//                                landslideItems.add(LandSlideItem(moisture, tilt, movement))
                                landslideItems.add(LandSlideItem(0.0, "test", "yes"))
                            }
                            "Rainfall" -> {

                            }
                        }
                    }
                    if (landslideItems.isNotEmpty()) {
                        setupRecyclerView(view, landslideItems, context)
                        view.findViewById<RecyclerView>(R.id.recycleview).visibility = View.VISIBLE
                        getTiltData(view,cityName)
                    } else {
                        Toast.makeText(context, "No landslide data available", Toast.LENGTH_SHORT).show()
                        Log.d("AlertsFragment", "No landslide items found")
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed to retrieve data: ${exception.message}", Toast.LENGTH_LONG).show()
                    Log.e("AlertsFragment", "Firestore error", exception)
                }
        }catch (error: Exception){

        }
    }
}