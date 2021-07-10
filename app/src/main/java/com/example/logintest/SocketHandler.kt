package com.example.logintest

import android.app.Activity
import android.content.Context
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(uri:String) {
        try{
            mSocket = IO.socket(uri)
        } catch(e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }
}