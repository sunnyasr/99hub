package com.example.a99hub.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.data.dataStore.LimitManager
import com.example.a99hub.data.dataStore.LoginManager
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.ActivityMainBinding
import com.example.a99hub.network.Api
import com.example.a99hub.network.SocketInstance
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import net.simplifiedcoding.data.responses.LimitResponse
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.text.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loginManager: LoginManager
    private lateinit var userManager: UserManager
    private lateinit var limitManager: LimitManager
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var compositeDisposable: CompositeDisposable
    private var token: String = ""

    //    private var mSocket: Socket? = null
    private var mSocket: Socket? = null
    val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment).navController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setProgress()
        limitManager = LimitManager(this)
        loginManager = LoginManager(this)
        userManager = UserManager(this)
        compositeDisposable = CompositeDisposable()
        userManager.username.asLiveData().observe(this, {
            binding.tvUsername.text = makeCapital(it.toString())
        })
        userManager.token.asLiveData().observe(this, {
            token = it
            getCoin(it)
        })
        limitManager.coin.asLiveData().observe(this, {
            binding.tvCoins.text = StringBuilder().append("Coins : ").append(it.toDouble().toInt())
        })

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnProfile.setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }
        //Socket instance
        val app: SocketInstance = application as SocketInstance
        mSocket = app.getMSocket()
//        //connecting socket
        mSocket!!.connect()
        val options = IO.Options()
        options.reconnection = true //reconnection
        options.forceNew = true
//
        if (mSocket?.connected()!!) {
            Toast.makeText(this, "Socket is connected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Socket is not connected", Toast.LENGTH_SHORT).show()
        }
//        mSocket!!.on(Socket.EVENT_CONNECT, Emitter.Listener {
//            mSocket!!.emit("event_id", "30324990")
//        });

//        try {
//            mSocket = IO.socket("https://index.bluexch.com")
//            mSocket?.let {
//                it.connect().on(Socket.EVENT_CONNECT) {
//                    Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()
//                }
//            }
//            mSocket?.connect()
//        } catch (e: Exception) {
//            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
//        }


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

    fun getCoin(token: String) {
        Api().getLimitCoins(token).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val data: String = response.body()?.string().toString()
                if (response.isSuccessful && response.code() == 200) {

                    if (!Common(this@MainActivity).checkJSONObject(data)) {

                        val jsonArray = JSONArray(data)
                        val obj = jsonArray.getJSONObject(0)
                        val limit = LimitResponse(
                            obj.getString("current"),
                            obj.getString("locked"),
                            obj.getString("hide_commission"),
                            obj.getString("new"),
                            obj.getString("username"),
                            obj.getString("name"),
                            obj.getString("valid"),
                        )

                        lifecycleScope.launch {
                            response.body()?.let { limitManager.store(limit) }
                        }
                    } else {
                        logout()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "[ERROR]" + t.message, Toast.LENGTH_LONG).show()
            }

        })

    }
}