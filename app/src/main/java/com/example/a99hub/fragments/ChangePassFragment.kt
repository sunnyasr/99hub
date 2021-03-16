package com.example.a99hub.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.example.a99hub.R
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.databinding.FragmentChangePassBinding
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class ChangePassFragment : Fragment() {

    private var _binding: FragmentChangePassBinding? = null
    private val binding get() = _binding!!
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var userManager: UserManager
    private lateinit var kProgressHUD: KProgressHUD
    private var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePassBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userManager = UserManager(requireContext())
        compositeDisposable = CompositeDisposable()
        setProgress()
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        userManager.token.asLiveData().observe(requireActivity(), {
            token = it.toString()
        })

        binding.changePass.setOnClickListener {
            val old: String = binding.etOld.text.toString().trim()
            val new: String = binding.etNew.text.toString().trim()
            val confirm: String = binding.etConfirm.text.toString().trim()

            if (TextUtils.isEmpty(old)) {
                TastyToast.makeText(
                    context,
                    "Enter Old Password",
                    TastyToast.LENGTH_LONG,
                    TastyToast.INFO
                )
            } else if (TextUtils.isEmpty(new)) {
                TastyToast.makeText(
                    context,
                    "Enter New Password",
                    TastyToast.LENGTH_LONG,
                    TastyToast.INFO
                )
            } else if (TextUtils.isEmpty(confirm)) {
                TastyToast.makeText(
                    context,
                    "Enter Confirm Password",
                    TastyToast.LENGTH_LONG,
                    TastyToast.INFO
                )
            } else if (!new.equals(confirm)) {
                TastyToast.makeText(
                    context,
                    "New Password And Confirm Password does not match",
                    TastyToast.LENGTH_LONG,
                    TastyToast.INFO
                )
            } else {
                changePass(token, old, new)
            }
        }


    }

    private fun changePass(token: String, old: String, new: String) {
        kProgressHUD.show()
        compositeDisposable.add(
            Api().changePass(token, old, new).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    kProgressHUD.dismiss()
                    if (it.status) {
                        TastyToast.makeText(
                            requireContext(),
                            it.msg,
                            TastyToast.LENGTH_LONG,
                            TastyToast.SUCCESS
                        )
                        binding.etOld.text=null
                        binding.etNew.text=null
                        binding.etConfirm.text=null
                    } else {
                        TastyToast.makeText(
                            requireContext(),
                            it.msg,
                            TastyToast.LENGTH_LONG,
                            TastyToast.ERROR
                        )
                    }
                }, {

                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}