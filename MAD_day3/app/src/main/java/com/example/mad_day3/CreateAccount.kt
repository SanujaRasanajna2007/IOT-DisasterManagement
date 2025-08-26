package com.example.mad_day3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mad_day3.Controller.CreateAcc
import kotlinx.coroutines.launch

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)
        val createAccBtn = findViewById<Button>(R.id.createAccountButton)
        val createAccObj by viewModels<CreateAcc>()
        createAccBtn.setOnClickListener {
            try{
                lifecycleScope.launch {
                    val result =  createAccObj.createUserAccount(
                        findViewById<EditText>(R.id.addressEditText).text.toString(),
                        findViewById<EditText>(R.id.cityEditText).text.toString(),
                        findViewById<EditText>(R.id.contactNoEditText).text.toString(),
                        findViewById<EditText>(R.id.emailEditText).text.toString(),
                        findViewById<EditText>(R.id.familyMembersEditText).text.toString().toInt(),
                        findViewById<EditText>(R.id.nameEditText).text.toString(),
                        findViewById<EditText>(R.id.passwordEditText).text.toString()
                    )
                }
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()
            }catch(e : Exception){
                Toast.makeText(this, "Error occured : ${e.message.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    }
}