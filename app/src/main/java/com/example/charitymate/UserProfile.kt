package com.example.charitymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.charitymate.databinding.UserProfileBinding
import com.google.firebase.auth.FirebaseAuth

class UserProfile : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: UserProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var contactTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        firebaseAuth = FirebaseAuth.getInstance()

        nameTextView = findViewById(R.id.upTVname)
        emailTextView = findViewById(R.id.upTVemail)
        usernameTextView = findViewById(R.id.upTVuname)
        contactTextView = findViewById(R.id.upTVTel)

        if (sessionManager.isLoggedIn()) {
            val name = sessionManager.getProfileName()
            val email = sessionManager.getProfileEmail()
            val username = sessionManager.getProfileUsername()
            val contact = sessionManager.getProfileContact()

            nameTextView.text = name
            emailTextView.text = email
            usernameTextView.text = username
            contactTextView.text = contact

        } else {
            // The user is not logged in, navigate to the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonLogOut.setOnClickListener {
            logout()
        }

        binding.buttonDash.setOnClickListener {
            val intent = Intent(this, HungerRelief::class.java)
            startActivity(intent)
        }
    }

    private fun logout() {
        // Clear the user session and log out
        sessionManager.clearSession()
        firebaseAuth.signOut()

        // Navigate back to the login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}