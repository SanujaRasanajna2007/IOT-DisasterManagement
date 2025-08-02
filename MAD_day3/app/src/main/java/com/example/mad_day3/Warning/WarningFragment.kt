package com.example.mad_day3.Warning

import com.example.mad_day3.Warning.WarningAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_day3.databinding.FragmentWarningBinding

class WarningFragment : Fragment() {
    private var _binding: FragmentWarningBinding? = null
    private val binding get() = _binding!!

    // Dummy data for warnings
    private val warningList = listOf(
        Warning(
            type = "Landslide",
            dangerLevel = "High",
            location = "Mountain Region",
            time = "Within 24 hours",
            description = "Heavy rainfall may trigger landslides in hilly areas"
        ),
        Warning(
            type = "Flood",
            dangerLevel = "Medium",
            location = "River Basin",
            time = "Next 48 hours",
            description = "River water levels rising due to continuous rain"
        ),
        Warning(
            type = "Earthquake",
            dangerLevel = "Low",
            location = "Northern Province",
            time = "Possible in coming week",
            description = "Minor seismic activity detected"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWarningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        binding.warningsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.warningsRecyclerView.adapter = WarningAdapter(warningList) { warning ->
            // Handle item click - show details
            WarningDetailDialog.newInstance(warning).show(
                parentFragmentManager,
                "WarningDetailDialog"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Data class for warnings
    data class Warning(
        val type: String,
        val dangerLevel: String,
        val location: String,
        val time: String,
        val description: String
    )
}