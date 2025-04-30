package com.example.landrenting.admin
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.landrenting.R
import com.example.landrenting.models.LandModel
import com.google.android.material.button.MaterialButton

class AdminPendingRequestsAdapter(
    private var lands: List<LandModel>,
    private val onApprove: (LandModel) -> Unit,
    private val onReject: (LandModel) -> Unit,
    private val onItemClick: (LandModel) -> Unit
) : RecyclerView.Adapter<AdminPendingRequestsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val landImage: ImageView = itemView.findViewById(R.id.landImage)
        val locationText: TextView = itemView.findViewById(R.id.locationText)
        val priceText: TextView = itemView.findViewById(R.id.priceText)
        val approveButton: MaterialButton = itemView.findViewById(R.id.approveButton)
        val rejectButton: MaterialButton = itemView.findViewById(R.id.rejectButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_pending_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val land = lands[position]

        // Load image using Glide
        if (land.imageUris.isNotEmpty()) {
            try {
                val uri = Uri.parse(land.imageUris[0])
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .placeholder(R.drawable.recimagetest)
                    .error(R.drawable.recimagetest)
                    .into(holder.landImage)
            } catch (e: Exception) {
                holder.landImage.setImageResource(R.drawable.recimagetest)
            }
        } else {
            holder.landImage.setImageResource(R.drawable.recimagetest)
        }

        holder.locationText.text = land.location ?: "No location"
        holder.priceText.text = String.format("%,.2f DH", land.price)

        // Set click listeners
        holder.itemView.setOnClickListener { onItemClick(land) }
        holder.approveButton.setOnClickListener { onApprove(land) }
        holder.rejectButton.setOnClickListener { onReject(land) }
    }

    override fun getItemCount(): Int = lands.size

    fun updateData(newLands: List<LandModel>) {
        lands = newLands
        notifyDataSetChanged()
    }
}