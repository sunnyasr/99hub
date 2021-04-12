package com.example.a99hub.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.asLiveData
import com.example.a99hub.R
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.ui.auth.LoginActivity
import com.example.a99hub.ui.home.HomeActivity
import com.example.a99hub.ui.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userManager: UserManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userManager.token.asLiveData().observe(this, {
            Handler(Looper.myLooper()!!).postDelayed({

                val activity =
                    if (it == null) LoginActivity::class.java else HomeActivity::class.java
                startNewActivity(activity)
            }, 1000)
        })


    }
}