package com.example.a99hub.ui.oldgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentOldGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OldGameFragment : Fragment(R.layout.fragment_old_game) {
    private lateinit var binding: FragmentOldGameBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOldGameBinding.bind(view)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

}