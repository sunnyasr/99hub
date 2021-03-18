package com.example.a99hub.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.common.Common
import com.example.a99hub.data.dataStore.ProfileManager
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.FragmentProfileBinding
import com.example.a99hub.network.Api
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var profileManager: ProfileManager
    private lateinit var spinner: MaterialSpinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        profileManager = activity?.let { ProfileManager(it.applicationContext) }!!
        compositeDisposable = CompositeDisposable()
        setProgress()
        getProfile(Token(requireContext()).getToken())


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




        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    fun getProfile(token: String) {
        kProgressHUD.show()
        compositeDisposable.add(
            Api().getProfile(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    run {
                        lifecycleScope.launch {
                            profileManager.storeUser(res.get(0))
                            kProgressHUD.dismiss()
                        }
                    }
                }, {
                    kProgressHUD.dismiss()
                    if (it.message.equals(Common(requireContext()).sessionError)) {

                        Log.d("ProfileError", it.message.toString())

                        run {
                            lifecycleScope.launch {
                                Common(requireContext()).logout()
                            }
                        }

                    } else {
                        Toast.makeText(context, "" + it.message, Toast.LENGTH_LONG).show()
                    }
                })
        )
    }

}