package com.example.logintest

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.logintest.adapters.WaitingRoomAdapter
import com.example.logintest.databinding.ActivityRoomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RoomActivity : AppCompatActivity() {
    lateinit var retrofit: Retrofit
    lateinit var retrofitInterface: RetrofitInterface
    var BASE_URL:String = "http://10.0.2.2:3000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        val listView = findViewById<ListView>(R.id.lv_rooms)

        var call = retrofitInterface.getRooms()

        lateinit var rooms: List<RoomResult>

        call.enqueue(object: Callback<List<RoomResult>>{
            override fun onResponse(
                call: Call<List<RoomResult>>,
                response: Response<List<RoomResult>>
            ) {
                rooms = response.body()!!
                listView.adapter = WaitingRoomAdapter(this@RoomActivity, rooms)
            }

            override fun onFailure(call: Call<List<RoomResult>>, t: Throwable) {
                Toast.makeText(this@RoomActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })

        var createBtn = findViewById<Button>(R.id.bt_create_room)
        createBtn.setOnClickListener {
            CreateRoomDialogFragment()
        }
    }
}