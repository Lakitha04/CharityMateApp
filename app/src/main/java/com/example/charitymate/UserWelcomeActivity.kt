package com.example.charitymate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UserWelcomeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_welcome)

        // Navigate to RegisterActivity when the "Register" button is clicked
        val registerButton = findViewById<Button>(R.id.button_Reg)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Navigate to LoginActivity when the "Login" button is clicked
        val loginButton = findViewById<Button>(R.id.button_login)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}