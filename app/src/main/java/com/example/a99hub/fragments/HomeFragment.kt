package com.example.a99hub.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }
        binding.inPlay.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_inPlayFragment)
        }
        binding.upcomingGame.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_upcomingGamesFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}