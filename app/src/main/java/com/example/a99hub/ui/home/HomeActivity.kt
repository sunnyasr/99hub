package com.example.a99hub.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.data.dataStore.LimitManager
import com.example.a99hub.data.dataStore.LoginManager
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.databinding.ActivityMainBinding
import com.example.a99hub.data.network.Api
import com.example.a99hub.data.network.Resource
import com.example.a99hub.ui.auth.LoginActivity
import com.example.a99hub.ui.utils.startNewActivity
import com.github.nkzawa.socketio.client.Socket
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.text.StringBuilder


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var limitManager: LimitManager
    private lateinit var kProgressHUD: KProgressHUD


    val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment).navController
    }

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setProgress()
        userManager.username.asLiveData().observe(this, {
            binding.tvUsername.text = it.toUpperCase()
        })

        viewModel.limitResponse.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    kProgressHUD.dismiss()
                    lifecycleScope.launch {
                        limitManager.store(it.value.get(0))
                    }
                }
                is Resource.Loading -> {
                    kProgressHUD.show()

                }
                is Resource.Failure -> {
                    kProgressHUD.dismiss()
                    performLogout()
                }
            }
        })

        val token = runBlocking { userManager.token.first() }
        token?.let { viewModel.getCoins(it) }

        limitManager.coin.asLiveData().observe(this, {
            binding.tvCoins.text = StringBuilder().append("Coins : ").append(it.toDouble().toInt())
        })

        binding.btnLogout.setOnClickListener {
            performLogout()
        }

        binding.btnProfile.setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }


    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(this@HomeActivity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    fun performLogout() = lifecycleScope.launch {

        viewModel.logout(userManager.token.first()!!)
        userManager.clear()
        limitManager.clear()
        startNewActivity(LoginActivity::class.java)
    }


    fun progress(yes: Boolean) {
        if (yes) kProgressHUD.show() else kProgressHUD.dismiss()
    }
}