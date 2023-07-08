package com.example.charitymate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.charitymate.databinding.HungerAdminItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class AdminHungerItemsAdapter(private var mList: List<HungerDetails>) :
    RecyclerView.Adapter<AdminHungerItemsAdapter.AdminHungerViewHolder>() {

    inner class AdminHungerViewHolder(var binding: HungerAdminItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminHungerViewHolder {
        val binding = HungerAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminHungerViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: AdminHungerViewHolder, position: Int) {
        with(holder.binding) {
            val item = mList[position]
            itemTitleTextView.text = item.title
            Picasso.get().load(item.pic).into(itemImageView)

            buttonDeleteHungerAdmin.setOnClickListener {
                val db = FirebaseFirestore.getInstance()
                db.collection("HungerDetails").document(item.id)
                    .delete()
                    .addOnSuccessListener {
                        mList = mList.filter { it.id != item.id }
                        notifyDataSetChanged()
                    }
            }

            buttonEditHungerAdmin.setOnClickListener {
                showUpdateForm(holder, item)
            }

        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun showUpdateForm(holder: AdminHungerViewHolder, item: HungerDetails) {
        // Inflate the update form layout
        val updateFormView = LayoutInflater.from(holder.itemView.context)
            .inflate(R.layout.update_form, null)

        // Get references to the views in the update form
        val titleEditText = updateFormView.findViewById<EditText>(R.id.updateTitle)
        val descriptionEditText = updateFormView.findViewById<EditText>(R.id.updateDescription)
        val contactEditText = updateFormView.findViewById<EditText>(R.id.updateContact)
        val startDateEditText = updateFormView.findViewById<EditText>(R.id.updateStartDate)
        val endDateEditText = updateFormView.findViewById<EditText>(R.id.updateEndDate)

        // Set the previous details in the update form
        titleEditText.setText(item.title)
        descriptionEditText.setText(item.description)
        contactEditText.setText(item.contact)
        startDateEditText.setText(item.startDate)
        endDateEditText.setText(item.endDate)

        // Show a dialog with the update form
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Update Item")
            .setView(updateFormView)
            .setPositiveButton("Update") { dialog, _ ->
                // Handle update button click
                val updatedTitle = titleEditText.text.toString()
                val updatedDescription = descriptionEditText.text.toString()
                val updatedContact = contactEditText.text.toString()
                val updatedStartDate = startDateEditText.text.toString()
                val updatedEndDate = endDateEditText.text.toString()
                val updatedPic = item.pic

                // Perform the update operation
                updateItem(item, updatedTitle, updatedDescription, updatedContact, updatedStartDate, updatedEndDate, updatedPic)

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

private fun updateItem(
    item: HungerDetails,
    updatedTitle: String,
    updatedDescription: String,
    updatedContact: String,
    updatedStartDate: String,
    updatedEndDate: String,
    updatedPic: String,
) {
    val db = FirebaseFirestore.getInstance()

    // Create the updated data HashMap
    val updatedData = hashMapOf(
        "title" to updatedTitle,
        "description" to updatedDescription,
        "contact" to updatedContact,
        "startDate" to updatedStartDate,
        "endDate" to updatedEndDate,
        "pic" to updatedPic,
        "amountNeeded" to item.amountNeeded,
        "location" to item.location
    )

    db.collection("HungerDetails").document(item.id)
        .set(updatedData)
        .addOnSuccessListener {
        }
}

}