package com.example.charitymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import com.example.charitymate.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth

class AdminProfile : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var binding: ActivityAdminProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var contactTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        firebaseAuth = FirebaseAuth.getInstance()

        nameTextView = findViewById(R.id.apTVname)
        emailTextView = findViewById(R.id.apTVemail)
        contactTextView = findViewById(R.id.apTVTel)

        if (sessionManager.isLoggedIn()) {
            val name = sessionManager.getProfileName()
            val email = sessionManager.getProfileEmail()
            val username = sessionManager.getProfileUsername()
            val contact = sessionManager.getProfileContact()

            nameTextView.text = name
            emailTextView.text = email
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

        val buttonDash: View = findViewById(R.id.button_dash)

        buttonDash.setOnClickListener {
            showPopupMenu(buttonDash)
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

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.popup_menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    val intent = Intent(this, AdminHunger::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_item2 -> {
                    val intent = Intent(this, AdminHealth::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_item3 -> {
                    val intent = Intent(this, AdminEducation::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_item4 -> {
                    val intent = Intent(this, AdminMf::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}