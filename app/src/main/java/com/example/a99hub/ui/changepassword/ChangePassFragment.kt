package com.example.a99hub.ui.changepassword

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.a99hub.R
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.FragmentChangePassBinding
import com.example.a99hub.data.network.Api
import com.example.a99hub.data.network.Resource
import com.example.a99hub.ui.inplay.InPlayGameViewModel
import com.example.a99hub.ui.utils.progress
import com.example.a99hub.ui.utils.toast
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ChangePassFragment : Fragment(R.layout.fragment_change_pass) {

    private lateinit var binding: FragmentChangePassBinding
    private val viewModel by viewModels<ChangePassViewModel>()

    @Inject
    lateinit var userManager: UserManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangePassBinding.bind(view)

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        viewModel.changePassResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    progress(true)
                }
                is Resource.Success -> {
                    progress(false)
                    if (it.value.status) {
                        toast(it.value.msg, TastyToast.SUCCESS)
                        activity?.onBackPressed()
                        binding.etOld.text = null
                        binding.etNew.text = null
                        binding.etConfirm.text = null
                    } else toast(it.value.msg, TastyToast.ERROR)
                }
                is Resource.Failure -> {
                    progress(false)
//                    Toast.makeText(requireContext(), "Error"+it.errorCode, Toast.LENGTH_SHORT).show()
                }
            }
        })


        binding.changePass.setOnClickListener {
            val old: String = binding.etOld.text.toString().trim()
            val new: String = binding.etNew.text.toString().trim()
            val confirm: String = binding.etConfirm.text.toString().trim()

            if (TextUtils.isEmpty(old)) {
                toast("Enter Old Password", TastyToast.INFO)
            } else if (TextUtils.isEmpty(new)) {
                toast("Enter New Password", TastyToast.INFO)
            } else if (TextUtils.isEmpty(confirm)) {
                toast("Enter Confirm Password", TastyToast.INFO)
            } else if (!new.equals(confirm)) {
                toast("New Password And Confirm Password does not match", TastyToast.INFO)
            } else {
                val token = runBlocking { userManager.token.first() }
                token?.let { viewModel.changePass(token, old, new) }
            }
        }

    }

}