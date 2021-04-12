package com.example.a99hub.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentHomeBinding
import com.example.a99hub.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        val navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }
        binding.inPlay.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_inPlayFragment)
        }
        binding.upcomingGame.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_upcomingGamesFragment)
        }
        binding.completeGame.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_completeGameFragment)
        }
        binding.profile.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_profileFragment)
        }
        binding.myLadger.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_ledgerFragment)
        }

        binding.changePassword.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_changePassFragment)
        }
        binding.oldGames.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_oldGameFragment)
        }

        binding.oldLedger.setOnClickListener {
            navController?.navigate(R.id.action_homeFragment_to_oldLedgerFragment)
        }

    }

}