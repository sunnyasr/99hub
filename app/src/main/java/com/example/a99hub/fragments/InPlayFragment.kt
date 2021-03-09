package com.example.a99hub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentInPlayBinding


class InPlayFragment : Fragment() {
    private var _binding: FragmentInPlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInPlayBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.carviwInplay.setOnClickListener {
            navController?.navigate(R.id.action_inPlayFragment_to_inplayDetailFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}