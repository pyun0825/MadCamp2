package com.example.logintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InnerRoomActivity : AppCompatActivity() {

    lateinit var retrofit: Retrofit
    lateinit var retrofitInterface: RetrofitInterface
    var BASE_URL:String = "http://143.248.226.140:3000"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_room)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        val roomName = intent.getStringExtra("roomName") as String
        findViewById<TextView>(R.id.tv_inner_title).text = roomName

        SocketHandler.setSocket()
        val mSocket = SocketHandler.getSocket()

        mSocket.connect()

        mSocket.emit("join room", roomName)

        mSocket.on("player join") {
            var map: HashMap<String, String> = HashMap()
            map.put("roomName", roomName)

            var call = retrofitInterface.enterRoom(map)
            call.enqueue(object : Callback<EnterRoomResult> {
                override fun onResponse(
                    call: Call<EnterRoomResult>,
                    response: Response<EnterRoomResult>
                ) {
                    var result = response.body()
                    findViewById<TextView>(R.id.tv_numPlayer).text = result?.numPlayer.toString()
                    findViewById<TextView>(R.id.tv_curPlayer).text = result?.curPlayer.toString()
                }

                override fun onFailure(call: Call<EnterRoomResult>, t: Throwable) {
                    Toast.makeText(this@InnerRoomActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }

//        var map:HashMap<String,String> = HashMap()
//        map.put("roomName", roomName)
//
//        var call = retrofitInterface.enterRoom(map)
//        call.enqueue(object: Callback<EnterRoomResult>{
//            override fun onResponse(
//                call: Call<EnterRoomResult>,
//                response: Response<EnterRoomResult>
//            ) {
//                var result = response.body()
//                findViewById<TextView>(R.id.tv_numPlayer).text = result?.numPlayer.toString()
//                findViewById<TextView>(R.id.tv_curPlayer).text = result?.curPlayer.toString()
//            }
//            override fun onFailure(call: Call<EnterRoomResult>, t: Throwable) {
//                Toast.makeText(this@InnerRoomActivity, t.message, Toast.LENGTH_LONG).show()
//            }
//        })
    }
}