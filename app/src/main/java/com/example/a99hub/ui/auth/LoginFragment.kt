package com.example.a99hub.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.R
import com.example.a99hub.ui.termcondition.TermConditionActivity
import com.example.a99hub.data.network.Resource
import com.example.a99hub.databinding.FragmentLoginBinding
import com.example.a99hub.ui.utils.handleApiError
import com.example.a99hub.ui.utils.startNewActivity
import com.example.a99hub.ui.utils.toast
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var kProgressHUD: KProgressHUD

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setProgress()

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    kProgressHUD.show()
                }
                is Resource.Success -> {

                    lifecycleScope.launch {
                        if (it.value.status) {
                            viewModel.saveAuthToken(it.value)
                            requireActivity().startNewActivity(TermConditionActivity::class.java)

                        } else
                            TastyToast.makeText(
                                requireActivity(),
                                "Invalid Username/Password try again",
                                Toast.LENGTH_LONG, TastyToast.ERROR
                            )

                    }

                }
                is Resource.Failure -> {
                    kProgressHUD.dismiss()
                    handleApiError(it) { login() }
                }
            }

        })

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(requireActivity())
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    private fun login() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (username.isEmpty()) {
            toast("Enter username",TastyToast.INFO)
        } else if (password.isEmpty()) {
            toast("Enter upassword",TastyToast.INFO)
        } else {
            viewModel.login(username, password, "")
        }
    }

}