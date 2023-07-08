package com.example.charitymate

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.charitymate.databinding.HungerItemBinding
import com.squareup.picasso.Picasso

class HungerItemsAdapter(private var mList: List<HungerDetails>) :
    RecyclerView.Adapter<HungerItemsAdapter.HungerViewHolder>() {

    inner class HungerViewHolder(var binding: HungerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HungerViewHolder {
        val binding = HungerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HungerViewHolder(binding)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HungerViewHolder, position: Int) {
        with(holder.binding) {
            val item = mList[position]
            itemTitleTextView.text = item.title
            itemDescriptionTextView.text = item.description
            itemLocationTextView.text = item.location
            itemAmountTextView.text = "Amount Needed: ${item.amountNeeded}"
            itemContactTextView.text = "Contact: ${item.contact}"
            itemStartDateTextView.text = "Start Date: ${item.startDate}"
            itemEndDateTextView.text = "End Date: ${item.endDate}"
            Picasso.get().load(item.pic).into(itemImageView)

            holder.binding.root.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, MakeAHungerDonation::class.java)
                intent.putExtra("amountNeeded", item.amountNeeded)
                intent.putExtra("documentId", item.id)
                Log.d("HungerItemsAdapter", "Amount Needed: ${item.amountNeeded}")
                Log.d("HungerItemsAdapter", "Document ID: ${item.id}")
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}