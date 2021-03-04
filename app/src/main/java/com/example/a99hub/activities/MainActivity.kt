package com.example.a99hub.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.data.dataStore.LoginManager
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.databinding.ActivityMainBinding
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import net.simplifiedcoding.data.responses.LogoutResponse
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loginManager: LoginManager
    private lateinit var userManager: UserManager
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var compositeDisposable: CompositeDisposable
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setProgress()
        loginManager = LoginManager(this)
        userManager = UserManager(this)
        compositeDisposable = CompositeDisposable()
        userManager.username.asLiveData().observe(this, {
            binding.tvUsername.text = makeCapital(it.toString())
        })
        userManager.token.asLiveData().observe(this, {
            token = it
        })

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    fun makeCapital(string: String): String {
        return string.substring(0, 1).toUpperCase(Locale.getDefault()) + string.substring(1)
            .toLowerCase(
                Locale.getDefault()
            )
    }

    fun logout() {

        kProgressHUD.show()
        compositeDisposable.add(
            Api().logout(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { res ->
                    if (res.status) {
                        lifecycleScope.launch {
                            loginManager.setLogged(false)
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                            kProgressHUD.dismiss()
                        }
                    }
                }, {
                    kProgressHUD.dismiss()
                    Toast.makeText(this, "Error tr again", Toast.LENGTH_LONG).show()
                })
        )

    }
}