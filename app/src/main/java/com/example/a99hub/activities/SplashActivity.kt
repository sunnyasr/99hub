package com.example.a99hub.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.example.a99hub.R
import com.example.a99hub.data.dataStore.LoginManager

class SplashActivity : AppCompatActivity() {
    private lateinit var loginManager: LoginManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loginManager = LoginManager(this)

        loginManager.isLogged.asLiveData().observe(this, {
            Handler().postDelayed({
                if (it == true)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                else
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }, 200)
        })


    }
}