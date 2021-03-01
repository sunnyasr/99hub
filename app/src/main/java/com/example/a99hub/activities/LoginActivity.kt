package com.example.a99hub.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a99hub.R
import com.example.a99hub.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        startActivity(Intent(applicationContext, MainActivity::class.java))

    }
}