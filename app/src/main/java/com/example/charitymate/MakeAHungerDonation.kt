package com.example.charitymate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MakeAHungerDonation : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var id: String
    private var amountNeeded: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.make_donation)

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance()

        id = intent.getStringExtra("documentId") ?: ""
        amountNeeded = intent.getDoubleExtra("amountNeeded", 0.0)

        val btnConfirm = findViewById<Button>(R.id.btnConfirmed)
        btnConfirm.setOnClickListener {
            onConfirmClicked()
        }
    }

    private fun onConfirmClicked() {
        val editAmount = findViewById<EditText>(R.id.editAmount)
        val spnPayMethod = findViewById<Spinner>(R.id.spnPayMethod)
        val editCardNumber = findViewById<EditText>(R.id.editCardNumber)
        val editDate = findViewById<EditText>(R.id.editDate)
        val editCVV = findViewById<EditText>(R.id.editCVV)

        // Retrieve values
        val amount = editAmount.text.toString()
        val paymentMethod = spnPayMethod.selectedItem.toString()
        val cardNumber = editCardNumber.text.toString()
        val expirationDate = editDate.text.toString()
        val cvv = editCVV.text.toString()

        //Validations
        if (amount.isEmpty()) {
            editAmount.error = "Amount is required"
            return
        }

        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            editAmount.error = "Invalid amount"
            return
        }

        if (cardNumber.isEmpty()) {
            editCardNumber.error = "Card number is required"
            return
        }

        if (expirationDate.isEmpty()) {
            editDate.error = "Expiration date is required"
            return
        }

        if (cvv.isEmpty()) {
            editCVV.error = "CVV is required"
            return
        }

        if (cvv.length != 3) {
            editCVV.error = "CVV should have 3 digits"
            return
        }

        // Save the payment to Firestore
        savePayment(amount, paymentMethod, cardNumber, expirationDate, cvv)
    }

    //savePayment function
    private fun savePayment(
        amount: String,
        paymentMethod: String,
        cardNumber: String,
        expirationDate: String,
        cvv: String
    ) {
        val amountValue = amount.toDoubleOrNull() ?: 0.0

        // Create a new document in the "Payments" collection in Firestore
        val payment = hashMapOf(
            "amount" to amountValue,
            "paymentMethod" to paymentMethod,
            "cardNumber" to cardNumber,
            "expirationDate" to expirationDate,
            "cvv" to cvv
        )

        firestore.collection("Payments")
            .add(payment)
            .addOnSuccessListener { documentReference ->
                // Payment saved successfully
                Toast.makeText(
                    this@MakeAHungerDonation,
                    "Payment saved successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // Fetch the current amount needed from HungerDetails database
                firestore.collection("HungerDetails").document(id)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val currentAmountNeeded = document.getDouble("amountNeeded") ?: 0.0
                            val donatedAmount = amountValue
                            val newAmountNeeded = if (currentAmountNeeded >= donatedAmount) currentAmountNeeded - donatedAmount else 0.0
                            Log.d("MakeAHungerDonation", "Current amount needed: $currentAmountNeeded")
                            Log.d("MakeAHungerDonation", "Donated amount: $donatedAmount")
                            Log.d("MakeAHungerDonation", "New amount needed: $newAmountNeeded")

                            // Update the new amount needed in the HungerDetails database
                            val updatedData = hashMapOf(
                                "amountNeeded" to newAmountNeeded
                            )
                            firestore.collection("HungerDetails").document(id)
                                .update(updatedData as Map<String, Any>)
                                .addOnSuccessListener {
                                    // New amount needed updated successfully
                                    Toast.makeText(
                                        this@MakeAHungerDonation,
                                        "Amount needed updated",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@MakeAHungerDonation,
                                        "Error updating amount needed: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    finish()
                                }
                        }
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@MakeAHungerDonation,
                    "Error saving payment: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}