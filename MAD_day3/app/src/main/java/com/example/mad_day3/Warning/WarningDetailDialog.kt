package com.example.mad_day3.Warning

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.mad_day3.R
import com.example.mad_day3.databinding.DialogWarningDetailBinding
import com.example.mad_day3.Warning.WarningFragment


class WarningDetailDialog : DialogFragment() {

    companion object {
        fun newInstance(warning: WarningFragment.Warning): WarningDetailDialog {
            return WarningDetailDialog().apply {
                arguments = Bundle().apply {
                    putString("type", warning.type)
                    putString("level", warning.dangerLevel)
                    putString("location", warning.location)
                    putString("time", warning.time)
                    putString("description", warning.description)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogWarningDetailBinding.inflate(requireActivity().layoutInflater)

        // Get warning details from arguments
        binding.warningType.text = arguments?.getString("type")
        binding.dangerLevel.text = "Danger Level: ${arguments?.getString("level")}"
        binding.affectedAreas.text = "Affected Areas: ${arguments?.getString("location")}"
        binding.timeFrame.text = "Time Frame: ${arguments?.getString("time")}"
        binding.warningDescription.text = arguments?.getString("description")

        // Set appropriate icon
        val iconRes = when (binding.warningType.text) {
            "Landslide" -> R.drawable.landslide_svgrepo_com
            "Flood" -> R.drawable.flood_warning_svgrepo_com
            else -> R.drawable.earthquake_svgrepo_com
        }
        binding.warningIcon.setImageResource(iconRes)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
    }
}