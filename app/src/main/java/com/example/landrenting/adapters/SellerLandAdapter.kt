package com.example.landrenting.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.R
import com.example.landrenting.data.entities.LandEntity
import com.example.landrenting.models.ApprovalStatus
import com.google.android.material.chip.Chip

class SellerLandAdapter(
    private var lands: List<LandEntity>
) : RecyclerView.Adapter<SellerLandAdapter.ViewHolder>() {

    private var onItemClickListener: ((LandEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (LandEntity) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val landImage: ImageView = itemView.findViewById(R.id.landImage)
        private val landName: TextView = itemView.findViewById(R.id.landName)
        private val landLocation: TextView = itemView.findViewById(R.id.landLocation)
        private val landPrice: TextView = itemView.findViewById(R.id.landPrice)
        private val statusChip: Chip = itemView.findViewById(R.id.statusChip)

        fun bind(land: LandEntity) {
            landName.text = land.landName
            landLocation.text = land.location
            landPrice.text = String.format("%,.2f DZD", land.price)

            // Set first image from the list
            val imageUris = land.imageUris.split(",")
            if (imageUris.isNotEmpty()) {
                try {
                    landImage.setImageURI(Uri.parse(imageUris[0]))
                } catch (e: Exception) {
                    landImage.setImageResource(R.drawable.recimagetest)
                }
            } else {
                landImage.setImageResource(R.drawable.recimagetest)
            }

            // Configure status chip
            statusChip.text = land.status.name
            when (land.status) {
                ApprovalStatus.PENDING -> {
                    statusChip.setChipBackgroundColorResource(R.color.pending_color)
                }
                ApprovalStatus.APPROVED -> {
                    statusChip.setChipBackgroundColorResource(R.color.approved_color)
                }
                ApprovalStatus.REJECTED -> {
                    statusChip.setChipBackgroundColorResource(R.color.rejected_color)
                }
            }

            itemView.setOnClickListener {
                onItemClickListener?.invoke(land)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seller_land, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lands[position])
    }

    override fun getItemCount() = lands.size

    fun updateData(newLands: List<LandEntity>) {
        lands = newLands
        notifyDataSetChanged()
    }
}