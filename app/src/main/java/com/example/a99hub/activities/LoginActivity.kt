package com.example.a99hub.activities

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.TextUtils
import android.text.format.Formatter
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a99hub.databinding.ActivityLoginBinding
import com.example.a99hub.network.Api
import net.simplifiedcoding.data.responses.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener(this)
    }

    fun getIPAddress(): String {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        return ipAddress
    }

    override fun onClick(v: View?) {

        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()


        if (TextUtils.isEmpty(username))
            Toast.makeText(applicationContext, "enter username", Toast.LENGTH_LONG).show()
        else if (TextUtils.isEmpty(password))
            Toast.makeText(applicationContext, "enter password", Toast.LENGTH_LONG).show()
        else {

            Api().uswrLogin(username, password, getIPAddress())
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        if (response.isSuccessful && response.code() == 200) {
                            val user = response.body()
//                            Toast.makeText(
//                                applicationContext,
//                                ,
//                                Toast.LENGTH_LONG
//                            ).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                        }

                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "[ERROR]" + t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                })

        }
    }
}