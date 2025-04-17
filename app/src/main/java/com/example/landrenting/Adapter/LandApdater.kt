package com.example.landrenting.Adapter

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.Model.LandModel
import com.example.landrenting.databinding.ItemPropertyBinding

class LandAdapter(
    private val lands: List<LandModel>,
    private val onItemClick: (LandModel) -> Unit
) : RecyclerView.Adapter<LandAdapter.LandViewHolder>() {

    inner class LandViewHolder(private val binding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(land: LandModel) {
            binding.imgRecHome.setImageResource(land.imageRes)
            binding.txtRecHome.text = land.location
            binding.priceRecHome.text = land.price
            binding.userNameRecHome.text = land.userName

            binding.outerCardView.setOnClickListener {
                onItemClick(land)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPropertyBinding.inflate(inflater, parent, false)
        return LandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LandViewHolder, position: Int) {
        holder.bind(lands[position])
    }

    override fun getItemCount(): Int = lands.size
}