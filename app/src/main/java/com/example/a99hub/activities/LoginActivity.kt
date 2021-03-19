package com.example.a99hub.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.data.dataStore.LoginManager
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.ActivityLoginBinding
import com.example.a99hub.network.Api
import com.example.a99hub.network.RetrofitClient
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.coroutines.launch
import net.simplifiedcoding.data.responses.LoginResponse
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.InetAddress
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginManager: LoginManager
    private lateinit var userManager: UserManager
    private lateinit var token: Token
    private lateinit var kProgressHUD: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            // JSON here
        } catch (e2: JSONException) {
            // TODO Auto-generated catch block
            e2.printStackTrace()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        token = Token(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginManager = LoginManager(this)
        userManager = UserManager(this)
        binding.btnLogin.setOnClickListener(this)
        setProgress()
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
        val ip = InetAddress.getLocalHost()
        return ip.toString().replace("localhost/", "")
    }

    override fun onClick(v: View?) {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (TextUtils.isEmpty(username))
            TastyToast.makeText(
                applicationContext,
                "Enter Username",
                Toast.LENGTH_LONG,
                TastyToast.INFO
            )
        else if (TextUtils.isEmpty(password))
            TastyToast.makeText(
                applicationContext,
                "Enter Password",
                Toast.LENGTH_LONG,
                TastyToast.INFO
            )
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
                                token.setToken(response.body()!!.token)
                                token.setUsername(response.body()!!.username)
                                Log.d("token_main", response.body()!!.token)
                                val intent =
                                    Intent(this@LoginActivity, TermConditionActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)

                            }


                        } else {
                            TastyToast.makeText(
                                this@LoginActivity,
                                "Invalid Username/Password try again",
                                Toast.LENGTH_LONG, TastyToast.ERROR
                            )
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