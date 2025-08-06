package com.example.mad_day3.Controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_day3.R

class landSlideAdapter(private val moisture: Double, private val tilt: String, private val slopeMovement: String)
    : RecyclerView.Adapter<landSlideAdapter.LandSlideCardViewHolder>() {
    class LandSlideCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardItem: LinearLayout = itemView.findViewById<LinearLayout>(R.id.landslideCard)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LandSlideCardViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_alerts,parent,false)
        return LandSlideCardViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: LandSlideCardViewHolder,
        position: Int
    ) {
        holder.cardItem.findViewById<TextView>(R.id.mointureLevel).text = moisture.toString()
        holder.cardItem.findViewById<TextView>(R.id.tiltStatus).text = tilt
        holder.cardItem.findViewById<TextView>(R.id.movementStatus).text = slopeMovement
    }

    override fun getItemCount(): Int {
        return 0
    }
}