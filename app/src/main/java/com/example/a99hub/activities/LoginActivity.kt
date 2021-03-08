package com.example.a99hub.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.data.dataStore.LoginManager
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.databinding.ActivityLoginBinding
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.launch
import net.simplifiedcoding.data.responses.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.InetAddress
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginManager: LoginManager
    private lateinit var userManager: UserManager
    private lateinit var kProgressHUD: KProgressHUD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
        loginManager = LoginManager(this)
        userManager = UserManager(this)
        binding.btnLogin.setOnClickListener(this)
        setProgress()

//        val thread = Thread {
//            try {
//                //Your code goes here
//                Toast.makeText(this,getIP(),Toast.LENGTH_LONG).show()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//        thread.start()

    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    fun getIP(): String {
//        val ip = InetAddress.getLocalHost()
//        return ip.toString()
        return ""
    }

    override fun onClick(v: View?) {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (TextUtils.isEmpty(username))
            Toast.makeText(applicationContext, "enter username", Toast.LENGTH_LONG).show()
        else if (TextUtils.isEmpty(password))
            Toast.makeText(applicationContext, "enter password", Toast.LENGTH_LONG).show()
        else {
            kProgressHUD.show()
            Api().userLogin(username, password, getIP())
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        kProgressHUD.dismiss()
                        if (response.body()?.status!!) {
                            lifecycleScope.launch {
                                loginManager.setLogged(true)
                                userManager.storeUser(response.body()!!)
                                val intent = Intent(this@LoginActivity, TermConditionActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)

                            }


                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Invalid Username/Password try again",
                                Toast.LENGTH_LONG
                            ).show()
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