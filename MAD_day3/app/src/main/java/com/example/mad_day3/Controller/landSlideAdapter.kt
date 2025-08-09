package com.example.mad_day3.Controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_day3.R
data class LandSlideItem(
    val moisture: Double,
    val tilt: String,
    val slopeMovement: String
)

class landSlideAdapter(private val items: List<LandSlideItem>)
    : RecyclerView.Adapter<landSlideAdapter.LandSlideCardViewHolder>() {

    class LandSlideCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moistureLevel: TextView = itemView.findViewById(R.id.mointureLevel)
        val tiltStatus: TextView = itemView.findViewById(R.id.tiltStatus)
        val movementStatus: TextView = itemView.findViewById(R.id.movementStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandSlideCardViewHolder {
        // Inflate the correct item layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.landslidecard, parent, false)
        return LandSlideCardViewHolder(view)
    }
    override fun onBindViewHolder(holder: LandSlideCardViewHolder, position: Int) {
        val item = items[position]
        holder.moistureLevel.text = "${item.moisture}%"
        holder.tiltStatus.text = item.tilt
        holder.movementStatus.text = item.slopeMovement
    }
    override fun getItemCount(): Int {
        // Return actual item count
        return items.size
    }
}