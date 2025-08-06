package com.example.mad_day3.Controller

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_day3.R
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

class loadCardsController {
    val db = Firebase.firestore
    val database = Firebase.database
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
                                val moisture = document.getDouble("moisture") ?: 0.0
                                val tilt = document.getString("tilt") ?: "Unknown"
                                val movement = document.getString("movement") ?: "Unknown"
                                landslideItems.add(LandSlideItem(moisture, tilt, movement))
                            }
                            "Rainfall" -> {

                            }
                        }
                    }
                    if (landslideItems.isNotEmpty()) {
                        setupRecyclerView(view, landslideItems, context)
                        view.findViewById<RecyclerView>(R.id.recycleview).visibility = View.VISIBLE
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