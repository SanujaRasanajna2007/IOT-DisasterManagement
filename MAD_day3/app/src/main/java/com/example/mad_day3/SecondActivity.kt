package com.example.mad_day3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        val btn2 = findViewById<Button>(R.id.backButton)
        val settings = findViewById<LinearLayout>(R.id.settingsTab)
        settings.setOnClickListener {
            val intent3 = Intent(this,ThirdActivity::class.java)
            startActivity(intent3)
        }
        btn2.setOnClickListener{
            val intent2 = Intent(this,MainActivity::class.java)
            startActivity(intent2)
        }
    }
}