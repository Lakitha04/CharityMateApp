package com.example.charitymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.charitymate.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.Signup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.LoginButton.setOnClickListener {
            val email = binding.EmailText.text.toString()
            val password = binding.PasswordText.text.toString()

            //Validations and implementation
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { loginTask ->
                    if (loginTask.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid
                        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(userId!!)
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                //If userId == "t8IPGqZjCOSgCI6SkNWqc2t1sHy1", then that user predefined as an Admin
                                if (userId == "t8IPGqZjCOSgCI6SkNWqc2t1sHy1") {
                                    // Redirect to AdminProfileActivity
                                    val adminRef = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
                                    adminRef.setValue(dataSnapshot.value)

                                    val name = dataSnapshot.child("name").value as? String
                                    val email = dataSnapshot.child("email").value as? String
                                    val username = dataSnapshot.child("username").value as? String
                                    val contact = dataSnapshot.child("contact").value as? String

                                    //Setting a session
                                    if (name != null && email != null && username != null && contact != null) {
                                        sessionManager.setLoggedIn(true)
                                        sessionManager.setProfileDetails(name, email, username, contact)

                                        val intent = Intent(this@LoginActivity, AdminProfile::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this@LoginActivity, "Failed to retrieve admin details", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    // Redirect to UserProfileActivity
                                    val name = dataSnapshot.child("name").value as? String
                                    val email = dataSnapshot.child("email").value as? String
                                    val username = dataSnapshot.child("username").value as? String
                                    val contact = dataSnapshot.child("contact").value as? String

                                    //Check whether the fields are empty or not
                                    if (name != null && email != null && username != null && contact != null) {
                                        sessionManager.setLoggedIn(true)
                                        sessionManager.setProfileDetails(name, email, username, contact)

                                        val intent = Intent(this@LoginActivity, UserProfile::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this@LoginActivity, "Failed to retrieve user details", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Toast.makeText(this@LoginActivity, databaseError.message, Toast.LENGTH_LONG).show()
                            }
                        })
                    } else {
                        Toast.makeText(this@LoginActivity, loginTask.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this@LoginActivity, "Empty fields are not allowed", Toast.LENGTH_LONG).show()
            }
        }
    }
}