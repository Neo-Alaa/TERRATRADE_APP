package com.example.landrenting.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.landrenting.models.LandModel
import com.example.landrenting.R

class SearchAdapter(private var landList: List<LandModel>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(land: LandModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val landImage: ImageView = itemView.findViewById(R.id.landImage)
        val landName: TextView = itemView.findViewById(R.id.landName)
        val landLocation: TextView = itemView.findViewById(R.id.landLocation)
        val landPrice: TextView = itemView.findViewById(R.id.landPrice)
        val landCategoryBadge: TextView = itemView.findViewById(R.id.landCategoryBadge)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(landList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = landList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val land = landList[position]
        val context = holder.itemView.context

        try {
            holder.landName.text = land.landName
            holder.landLocation.text = land.location
            holder.landPrice.text = String.format("%,.2f DH", land.price)

            // Load image using Glide with proper error handling
            if (land.imageUris.isNotEmpty()) {
                try {
                    val uri = Uri.parse(land.imageUris[0])
                    Glide.with(context)
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

            // Set category badge
            holder.landCategoryBadge.visibility = View.VISIBLE
            when (land.category.lowercase()) {
                "sell" -> {
                    holder.landCategoryBadge.apply {
                        text = context.getString(R.string.for_sale)
                        backgroundTintList = ContextCompat.getColorStateList(context, R.color.sale_badge)
                    }
                }
                "rent" -> {
                    holder.landCategoryBadge.apply {
                        text = context.getString(R.string.for_rent)
                        backgroundTintList = ContextCompat.getColorStateList(context, R.color.rent_badge)
                    }
                }
                else -> holder.landCategoryBadge.visibility = View.GONE
            }
        } catch (e: Exception) {
            // If any view binding fails, log the error but don't crash
            e.printStackTrace()
        }
    }

    fun updateData(newList: List<LandModel>) {
        this.landList = newList
        notifyDataSetChanged()
    }
}
