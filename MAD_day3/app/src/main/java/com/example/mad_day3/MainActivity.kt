package com.example.mad_day3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad_day3.Controller.CreateAcc
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnClick = findViewById<Button>(R.id.lgoInButton)
        val createAccBtn = findViewById<Button>(R.id.buttonCreateAcc)
        createAccBtn.setOnClickListener {
            val intentCreateAcc = Intent(this, CreateAccount::class.java)
            startActivity(intentCreateAcc)
        }
        btnClick.setOnClickListener{
            val email = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val PREF_NAME = "prefs"
            val KEY_NAME = "city"
            val userID_key = "userID"
            lateinit var sharedPref : SharedPreferences
            sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            db.collection("userAccounts")
                .whereEqualTo("email",email)
                .get()
                .addOnSuccessListener { result ->
                    if(result.isEmpty){
                        Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show()
                    }else{
                        for (document in result) {
                            val storedPassword = document.getString("password") ?: ""
                            if (password == storedPassword) {
                                val editor = sharedPref.edit().putString(KEY_NAME, document.getString("city")).apply()
                                val editor2 = sharedPref.edit().putString(userID_key, document.id).apply()
                                //TODO: TEST
                                Log.e("DEBUG", "User ID: ${document.id}")
                                val intent2 = Intent(this, SecondActivity::class.java)
                                startActivity(intent2)
                                return@addOnSuccessListener
                            } else {
                                Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error occured : ${exception.message.toString()}", Toast.LENGTH_LONG).show()
                }
        }
    }
}