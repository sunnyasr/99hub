package com.example.a99hub.data.network

import android.app.Application
import android.util.Log
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URISyntaxException

//private const val URL = "https://index.bluexch.com/"
private const val URL = "http://192.168.43.44:3000"

class SocketInstance : Application() {

    private var mSocket: Socket? = null

    override fun onCreate() {
        super.onCreate()
        try {
            //creating socket instance

            mSocket = IO.socket(URL)
//            mSocket.let {
//                it!!.connect()
//                    .on(Socket.EVENT_CONNECT) {
//                        Log.d("SignallingClient", "Socket connected!!!!!")
//                    }
//            }

        } catch (e: URISyntaxException) {

            Log.i("socket_99", e.message.toString())
            throw RuntimeException(e)

        }
    }

    //    return socket instance
    fun getMSocket(): Socket? {
        return mSocket
    }
}