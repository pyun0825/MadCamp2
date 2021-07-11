package com.example.logintest

import android.content.Intent
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
    lateinit var BASE_URL:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_room)
        BASE_URL = getString(R.string.ip_address)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        val roomName = intent.getStringExtra("roomName") as String
        val game_id = intent.getStringExtra("game_id")
        var num_player:Int = 0

        findViewById<TextView>(R.id.tv_inner_title).text = roomName

        SocketHandler.setSocket(BASE_URL)
        val mSocket = SocketHandler.getSocket()

        mSocket.connect()


        var map: HashMap<String, String> = HashMap()
        map.put("roomName", roomName)
        var call = retrofitInterface.enterRoom(map)
        call.enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                System.out.println("Entered room & incremented cur_player")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                System.out.println("Failed to Enter room")
            }
        })
        mSocket.emit("join room", roomName, game_id)

        mSocket.on("player join") {
            var call2 = retrofitInterface.updateRoom(map)
            call2.enqueue(object : Callback<EnterRoomResult> {
                override fun onResponse(
                    call: Call<EnterRoomResult>,
                    response: Response<EnterRoomResult>
                ) {
                    var result = response.body()
                    num_player = result?.numPlayer!!
                    findViewById<TextView>(R.id.tv_numPlayer).text = "Players needed: "+result?.numPlayer.toString()
                    findViewById<TextView>(R.id.tv_curPlayer).text = "Current players: "+result?.curPlayer.toString()
                }

                override fun onFailure(call: Call<EnterRoomResult>, t: Throwable) {
                    Toast.makeText(this@InnerRoomActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
        mSocket.on("to game"){
            val intent = Intent(this@InnerRoomActivity, GameActivity::class.java).apply {
                putExtra("roomName", roomName)
                putExtra("game_id", game_id)
                putExtra("num_player", num_player)
            }
            mSocket.off("to game")
            startActivity(intent)
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