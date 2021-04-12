package com.example.a99hub.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.R
import com.example.a99hub.data.dataStore.ProfileManager
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.databinding.FragmentProfileBinding
import com.example.a99hub.data.network.Resource
import com.example.a99hub.ui.utils.logout
import com.example.a99hub.ui.utils.progress
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var profileManager: ProfileManager
    private lateinit var spinner: MaterialSpinner


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        spinner = binding.spinner

        spinner.setItems("0", "1", "2", "3", "4", "5")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Clicked $item",
                Snackbar.LENGTH_LONG
            ).show()
        }

        profileManager.name.asLiveData().observe(requireActivity(), {
            binding.tvCname.text = it
            if (it.equals(""))
                progress(true)
        })
        profileManager.username.asLiveData().observe(requireActivity(), {
            binding.tvCid.text = it
        })

        profileManager.mobile.asLiveData().observe(requireActivity(), {
            binding.tvContact.text = it
        })
        profileManager.joinDate.asLiveData().observe(requireActivity(), {
            binding.tvDoj.text = it
        })


        viewModel.profileResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    progress(true)
                }
                is Resource.Success -> {
                    lifecycleScope.launch {
                        progress(false)
                        profileManager.storeUser(it.value.get(0))
                    }
                }
                is Resource.Failure -> {
                    logout()
                }
            }
        })

        val token = runBlocking { userManager.token.first() }
        token?.let { viewModel.getProfile(it) }

    }


}