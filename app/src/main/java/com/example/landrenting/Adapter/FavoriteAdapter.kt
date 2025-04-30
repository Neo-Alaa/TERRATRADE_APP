package com.example.landrenting.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.landrenting.R
import com.example.landrenting.models.LandModel

class FavoriteAdapter(private var lands: List<LandModel>) : 
    RecyclerView.Adapter<FavoriteAdapter.LandViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null
    private var favoriteClickListener: ((LandModel) -> Unit)? = null

    interface OnItemClickListener {
        fun onItemClick(land: LandModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun setOnFavoriteClickListener(listener: (LandModel) -> Unit) {
        this.favoriteClickListener = listener
    }

    fun updateData(newLands: List<LandModel>) {
        this.lands = newLands
        notifyDataSetChanged()
    }

    inner class LandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgRecHome: ImageView = itemView.findViewById(R.id.imgRecHome)
        private val txtRecHome: TextView = itemView.findViewById(R.id.txtRecHome)
        private val priceRecHome: TextView = itemView.findViewById(R.id.priceRecHome)
        private val userNameRecHome: TextView = itemView.findViewById(R.id.userNameRecHome)
        private val outerCardView: CardView = itemView.findViewById(R.id.outer_cardView)
        private val imgHeart: ImageView = itemView.findViewById(R.id.favRecFragment)

        fun bind(land: LandModel) {
            // Load image using Glide
            if (land.imageUris.isNotEmpty()) {
                try {
                    val uri = Uri.parse(land.imageUris[0])
                    Glide.with(itemView.context)
                        .load(uri)
                        .placeholder(R.drawable.recimagetest)
                        .error(R.drawable.recimagetest)
                        .into(imgRecHome)
                } catch (e: Exception) {
                    imgRecHome.setImageResource(R.drawable.recimagetest)
                }
            } else {
                imgRecHome.setImageResource(R.drawable.recimagetest)
            }
            
            txtRecHome.text = land.location
            userNameRecHome.text = land.landName
            priceRecHome.text = String.format("%,.2f DH", land.price)

            val heartDrawableRes = if (land.isFavorite) R.drawable.redheard else R.drawable.heartabd
            imgHeart.setImageResource(heartDrawableRes)

            imgHeart.setOnClickListener {
                favoriteClickListener?.invoke(land)
            }

            outerCardView.setOnClickListener {
                itemClickListener?.onItemClick(land)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fav_item_property, parent, false)
        return LandViewHolder(view)
    }

    override fun onBindViewHolder(holder: LandViewHolder, position: Int) {
        holder.bind(lands[position])
    }

    override fun getItemCount(): Int = lands.size
}