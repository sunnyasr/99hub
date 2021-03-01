package com.example.a99hub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }
        binding.btnLogin.setOnClickListener {

            navController?.navigate(R.id.action_loginFragment_to_homeFragment)
        }
        return binding.root
    }
}