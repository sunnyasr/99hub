package com.example.a99hub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentCompleteGameBinding


class CompleteGameFragment : Fragment() {
    private var _binding: FragmentCompleteGameBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteGameBinding.inflate(layoutInflater, container, false)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}