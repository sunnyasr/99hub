package com.example.a99hub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentUpcomingGamesBinding


class UpcomingGamesFragment : Fragment() {
    private var _binding: FragmentUpcomingGamesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingGamesBinding.inflate(layoutInflater, container, false)
        binding.btnBack.setOnClickListener {
            it.findNavController().navigate(R.id.action_upcomingGamesFragment_to_homeFragment)
        }
        return binding.root
    }

}