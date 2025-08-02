package com.example.mad_day3.Warning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_day3.R
import com.example.mad_day3.databinding.ItemWarningBinding
import com.example.mad_day3.Warning.WarningFragment


class WarningAdapter(
    private val warnings: List<WarningFragment.Warning>,
    private val onItemClick: (WarningFragment.Warning) -> Unit
) : RecyclerView.Adapter<WarningAdapter.WarningViewHolder>() {

    inner class WarningViewHolder(private val binding: ItemWarningBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(warning: WarningFragment.Warning) {
            binding.warningType.text = warning.type
            binding.warningLocation.text = warning.location
            binding.warningTime.text = warning.time

            // Set danger level color
            when (warning.dangerLevel) {
                "High" -> binding.dangerLevelIndicator.setCardBackgroundColor(
                    binding.root.context.getColor(android.R.color.holo_red_light)
                )
                "Medium" -> binding.dangerLevelIndicator.setCardBackgroundColor(
                    binding.root.context.getColor(android.R.color.holo_orange_light)
                )
                else -> binding.dangerLevelIndicator.setCardBackgroundColor(
                    binding.root.context.getColor(android.R.color.holo_green_light)
                )
            }

            // Set warning icon based on type
            val iconRes = when (warning.type) {
                "Landslide" -> R.drawable.landslide_svgrepo_com
                "Flood" -> R.drawable.flood_warning_svgrepo_com
                else -> R.drawable.earthquake_svgrepo_com
            }
            binding.warningIcon.setImageResource(iconRes)

            binding.root.setOnClickListener { onItemClick(warning) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarningViewHolder {
        val binding = ItemWarningBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WarningViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WarningViewHolder, position: Int) {
        holder.bind(warnings[position])
    }

    override fun getItemCount() = warnings.size
}