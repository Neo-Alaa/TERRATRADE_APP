package com.example.landrenting.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.landrenting.R

class ImagePagerAdapter : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
    private var images: List<Uri> = emptyList()

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_pager, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = images[position]
        Glide.with(holder.itemView.context)
            .load(uri)
            .placeholder(R.drawable.recimagetest)
            .error(R.drawable.recimagetest)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = images.size

    fun updateImages(newImages: List<Uri>) {
        images = newImages
        notifyDataSetChanged()
    }
}