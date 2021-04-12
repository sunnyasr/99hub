package com.example.a99hub.ui.auth

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import com.example.a99hub.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
//        try {
//            // JSON here
//        } catch (e2: JSONException) {
//            // TODO Auto-generated catch block
//            e2.printStackTrace()
//        } catch (e: Exception) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

//    fun getIP(): String {
//        var ip: String = ""
//        try {
//            ip = InetAddress.getLocalHost().toString()
//        } catch (e: NetworkOnMainThreadException) {
//            Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG).show()
//        }
//        Toast.makeText(applicationContext, ip, Toast.LENGTH_LONG).show()
//        return ip.toString().replace("localhost/", "")
//    }
//

}