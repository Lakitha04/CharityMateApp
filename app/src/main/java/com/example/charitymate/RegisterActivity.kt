package com.example.charitymate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.charitymate.databinding.RegisterPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterPageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.buttonRegister.setOnClickListener {
            val name = binding.regAsAGiverName.text.toString()
            val email = binding.regAsAGiverEmail.text.toString()
            val username = binding.regAsAGiverUname.text.toString()
            val contact = binding.regAsAGiverTel.text.toString()
            val password = binding.regAsAGiverPassword.text.toString()
            val rePassword = binding.regAsAGiverRePassword.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty() && username.isNotEmpty() && contact.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty()){
                if(password == rePassword){

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){

                            val user = firebaseAuth.currentUser
                            val userId = user?.uid ?:""
                            val userRef = database.reference.child("Users").child(userId)
                            val userData = mapOf(
                                "name" to name,
                                "email" to email,
                                "username" to username,
                                "contact" to contact,
                                "password" to password
                            )
                            userRef.setValue(userData)

                            val intent = Intent(this, UserWelcomeActivity::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }

                }else{
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_LONG).show()
            }
        }


        }
}