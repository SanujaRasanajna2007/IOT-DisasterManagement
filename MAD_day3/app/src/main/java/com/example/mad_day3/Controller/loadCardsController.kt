package com.example.mad_day3.Controller

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_day3.Model.landslideGroundMotions
import com.example.mad_day3.Model.landslideTilt
import com.example.mad_day3.Model.rainfallModel
import com.example.mad_day3.Model.waterLevelModel
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
    var change : Double = 0.0
    var runOnce: Int = 0
    var dangerStatus : Int = 0
    val db = Firebase.firestore
    val database = FirebaseDatabase.getInstance()
    val landslideMovementRef: DatabaseReference = database.getReference("Sensors/MPU6050Readings")
    val landslideTiltRef: DatabaseReference = database.getReference("Sensors/TiltReadings")
    val RainfallRef: DatabaseReference = database.getReference("Sensors/BMP180Readings")
    val WaterLevelRef: DatabaseReference = database.getReference("Sensors/waterLevelSensor/levelData")
    private var tiltEventListener: ValueEventListener? = null
    private var MotionEventListener2: ValueEventListener? = null
    private var rainfallListenerInputSection: ValueEventListener? = null
    private var waterLevelInputSection: ValueEventListener? = null
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
//                        if(it.location == cityName){
//                            if(it.value == 1){
//                                dangerStatus++
//                                view.findViewById<TextView>(R.id.tiltStatus)?.text =
//                                    "YES: " + it.value?.toString() ?: "N/A"
//                            }else{
//                                view.findViewById<TextView>(R.id.tiltStatus)?.text =
//                                    "NO: " + it.value?.toString() ?: "N/A"
//                            }
//
//                        }
                        if(it.value == 1){
                            dangerStatus++
                            view.findViewById<TextView>(R.id.tiltStatus)?.text =
                                "YES: " + it.value?.toString() ?: "N/A"
                        }else{
                            view.findViewById<TextView>(R.id.tiltStatus)?.text =
                                "NO: " + it.value?.toString() ?: "N/A"
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
    public fun getMotionData(view: View, cityName: String?){
        MotionEventListener2?.let { landslideMovementRef.removeEventListener(it) }
        MotionEventListener2 = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var latestMotionReading: landslideGroundMotions? = null
                    for (childSnapshot in snapshot.children){
                        val reading = childSnapshot.getValue(landslideGroundMotions::class.java)
                        reading?.let{
                            if (it.timestamp != null) {
                                if (latestMotionReading == null ||
                                    (latestMotionReading?.timestamp != null &&
                                            it.timestamp > latestMotionReading!!.timestamp)) {
                                    latestMotionReading = it
                                }
                            }
                        }
                    }
                    latestMotionReading?.let {
                        if(runOnce == 0){
                            change = it.accelX
                            runOnce = 2
                        }
                        if(change<it.accelX){
                            dangerStatus++
                            if(dangerStatus == 2){
                                view.findViewById<TextView>(R.id.statusDetail)?.text =
                                    "● WARNING" ?: "N/A"
                                dangerStatus = 0
                            }
                            view.findViewById<TextView>(R.id.movementStatus)?.text =
                                "YES: " +  it.accelX?.toString() ?: "N/A"
                        }else{
                            view.findViewById<TextView>(R.id.movementStatus)?.text =
                                 "NO: "+ it.accelX?.toString() ?: "N/A"
                        }
                        change = it.accelX

//                        if(it.location == cityName){
//                            view.findViewById<TextView>(R.id.movementStatus)?.text =
//                                it.accelX?.toString() ?: "N/A"
//                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Data read failed (motion): ${error.message}")
            }

        }
        MotionEventListener2?.let { landslideMovementRef.addValueEventListener(it) }
    }
    public fun getRainfallData(view: View, cityName: String?){
        rainfallListenerInputSection?.let { RainfallRef.removeEventListener(it) }
        rainfallListenerInputSection = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var latestRainfallReading: rainfallModel? = null
                    for (childSnapshot in snapshot.children){
                        val reading = childSnapshot.getValue(rainfallModel::class.java)
                        reading?.let{
                            if (it.timestamp != null) {
                                if (latestRainfallReading == null ||
                                    (latestRainfallReading?.timestamp != null &&
                                            it.timestamp > latestRainfallReading!!.timestamp)) {
                                    latestRainfallReading = it
                                }
                            }
                        }
                    }
                    latestRainfallReading?.let {
//                        if(it.location == cityName){
//                            view.findViewById<TextView>(R.id.lastUpdated4)?.text =
//                                it.altitude_m?.toString() ?: "N/A"
//                        }
                        view.findViewById<TextView>(R.id.preasureAmu)?.text = it.pressure_hPa?.toString() ?: "N/A"
                        view.findViewById<TextView>(R.id.altitudeAmu)?.text = it.altitude_m?.toString() ?: "N/A"
                        view.findViewById<TextView>(R.id.tempAmu)?.text = it.temperature_C?.toString() ?: "N/A"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Data read failed (rainfall): ${error.message}")
            }

        }
        rainfallListenerInputSection?.let { RainfallRef.addValueEventListener(it) }
    }
    fun getWaterLevelData(view: View, cityName: String?){
        waterLevelInputSection?.let { WaterLevelRef.removeEventListener(it) }
        rainfallListenerInputSection = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var latestwaterLevelReading: waterLevelModel? = null
                    for (childSnapshot in snapshot.children){
                        val reading = childSnapshot.getValue(waterLevelModel::class.java)
                        reading?.let{
                            if (it.time != null) {
                                if (latestwaterLevelReading == null ||
                                    (latestwaterLevelReading?.time != null &&
                                            it.time > latestwaterLevelReading!!.time)) {
                                    latestwaterLevelReading = it
                                }
                            }
                        }
                    }
                    latestwaterLevelReading?.let {
//                        if(it.location == cityName){
//                            view.findViewById<TextView>(R.id.waterLevelAmu)?.text =
//                                it.WaterLevelPercentage?.toString() ?: "N/A"
//                        }
                        view.findViewById<TextView>(R.id.waterLevelAmu)?.text = it.WaterLevelPercentage?.toString() ?: "N/A"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Data read failed (waterlevel): ${error.message}")
            }
        }
            waterLevelInputSection?.let { WaterLevelRef.addValueEventListener(it) }
        }
     fun setupRecyclerView(view: View, items: List<LandSlideItem>, context: Context) {
        val landSlideRecyclerView = view.findViewById<RecyclerView>(R.id.recycleview)
        landSlideRecyclerView.layoutManager = LinearLayoutManager(context)
        landSlideRecyclerView.adapter = landSlideAdapter(items)

        // Add this to verify if adapter is set
        Log.d("AlertsFragment", "RecyclerView adapter set with ${items.size} items")
    }
     fun setupRecyclerViewRainfall(view: View, items: List<rainfallItem>, context: Context){
        val RainfallRecyclerView = view.findViewById<RecyclerView>(R.id.recycleview)
        RainfallRecyclerView.layoutManager = LinearLayoutManager(context)
        RainfallRecyclerView.adapter = rainfallAdapter(items)
    }
     fun getLandslideCard(view: View, savedInstanceState: Bundle?, context: Context, cityName: String?){
        try {
            db.collection("locationInfo")
                .whereEqualTo("name", cityName)
                .get()
                .addOnSuccessListener { result ->
                    Log.d("AlertsFragment", "Found ${result.size()} documents")
                    val landslideItems = mutableListOf<LandSlideItem>()
                    val rainfallItems = mutableListOf<rainfallItem>()
                    for (document in result) {
                        when (document.getString("category")) {
                            "Landslide" -> {
                                landslideItems.add(LandSlideItem(0.0, "test", ""))
                            }
                            "Rainfall" -> {
                                rainfallItems.add(rainfallItem(0.0,0.0,0.0))
                            }
                        }
                    }
                    if(rainfallItems.isNotEmpty()){
                        setupRecyclerViewRainfall(view, rainfallItems, context)
                        view.findViewById<RecyclerView>(R.id.recycleview).visibility = View.VISIBLE
                        getRainfallData(view,cityName)
                        getWaterLevelData(view,cityName)
                    }else {
                        Toast.makeText(context, "No rainfall data available", Toast.LENGTH_SHORT).show()
                        Log.d("AlertsFragment", "No landslide items found")
                    }
                    if (landslideItems.isNotEmpty()) {
                        setupRecyclerView(view, landslideItems, context)
                        view.findViewById<RecyclerView>(R.id.recycleview).visibility = View.VISIBLE
                        getTiltData(view,cityName)
                        getMotionData(view,cityName)
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