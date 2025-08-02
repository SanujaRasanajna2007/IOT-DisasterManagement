package com.example.mad_day3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mad_day3.databinding.FragmentSettingsBinding
import androidx.fragment.app.Fragment

class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize view binding
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set dummy user data (frontend only)
        binding.profileName.text = "Demo User"
        binding.profileEmail.text = "demo@disasterapp.com"

        // Set click listeners for UI elements
        binding.btnEditProfile.setOnClickListener {
            // Handle profile edit button click
            // This would navigate to another fragment in a real app
        }

        binding.optionNotifications.setOnClickListener {
            // Handle notifications option click
        }

        binding.optionTheme.setOnClickListener {
            // Handle theme option click
        }

        binding.optionAbout.setOnClickListener {
            // Handle about option click
        }

        binding.optionContact.setOnClickListener {
            // Handle contact option click
        }

        binding.btnLogout.setOnClickListener {
            // Handle logout button click
            // In a real app, this would clear user session
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up view binding
        _binding = null
    }


}