package com.example.a99hub.network

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URISyntaxException

private const val URL = "http://192.168.43.44:5000/"

class SocketInstance : Application() {
    //socket.io connection url
    private var mSocket: Socket? = null

    override fun onCreate() {
        super.onCreate()
        try {
//creating socket instance

            mSocket = IO.socket(URL)
            mSocket!!.connect()
        } catch (e: URISyntaxException) {

            Log.i("socket_99", e.message.toString())
            throw RuntimeException(e)

        }
    }

    //return socket instance
    fun getMSocket(): Socket? {
        return mSocket
    }
}