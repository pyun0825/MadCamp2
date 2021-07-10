package com.example.logintest

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.logintest.adapters.WaitingRoomAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RoomActivity : AppCompatActivity() {
    lateinit var retrofit: Retrofit
    lateinit var retrofitInterface: RetrofitInterface
    lateinit var BASE_URL:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        BASE_URL = getString(R.string.ip_address)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        val game_id = intent.getStringExtra("game_id")

        val listView = findViewById<ListView>(R.id.lv_rooms)

        var call = retrofitInterface.getRooms()

        lateinit var rooms: List<RoomResult>

        call.enqueue(object: Callback<List<RoomResult>>{
            override fun onResponse(
                call: Call<List<RoomResult>>,
                response: Response<List<RoomResult>>
            ) {
                if(response.code() == 200){
                    rooms = response.body()!!
                    listView.adapter = WaitingRoomAdapter(this@RoomActivity, rooms, game_id)
                } else if(response.code() == 404){
                    rooms = emptyList()
                    System.out.println("No Rooms made!")
                }
            }

            override fun onFailure(call: Call<List<RoomResult>>, t: Throwable) {
                Toast.makeText(this@RoomActivity, t.message, Toast.LENGTH_LONG).show()
                rooms = emptyList()
            }
        })

        var createBtn = findViewById<Button>(R.id.bt_create_room)
        createBtn.setOnClickListener {
            val dialog = CreateRoomDialogFragment(this@RoomActivity, retrofitInterface, game_id)
            dialog.show(supportFragmentManager, "createRoomDialog")
        }

        var refreshLayout =findViewById<SwipeRefreshLayout>(R.id.rl_rooms)
        refreshLayout.setOnRefreshListener {
            var call = retrofitInterface.getRooms()

            lateinit var rooms: List<RoomResult>

            call.enqueue(object: Callback<List<RoomResult>>{
                override fun onResponse(
                    call: Call<List<RoomResult>>,
                    response: Response<List<RoomResult>>
                ) {
                    if(response.code() == 200){
                        rooms = response.body()!!
                        listView.adapter = WaitingRoomAdapter(this@RoomActivity, rooms, game_id)
                    } else if(response.code() == 404){
                        rooms = emptyList()
                        System.out.println("No Rooms made!")
                    }
                }

                override fun onFailure(call: Call<List<RoomResult>>, t: Throwable) {
                    Toast.makeText(this@RoomActivity, t.message, Toast.LENGTH_LONG).show()
                    rooms = emptyList()
                }
            })
            refreshLayout.isRefreshing = false
        }
    }
}