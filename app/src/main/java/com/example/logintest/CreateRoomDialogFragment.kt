package com.example.logintest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateRoomDialogFragment(context: Context, var retrofitInterface: RetrofitInterface, var game_id: String?) : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val RootView = inflater.inflate(R.layout.fragment_create_room_dialog, container, false)
        val cancelBtn = RootView.findViewById<Button>(R.id.bt_room_cancel)
        val createBtn = RootView.findViewById<Button>(R.id.bt_room_submit)
        val roomName = RootView.findViewById<EditText>(R.id.et_room_name)
        val numPlayer = RootView.findViewById<EditText>(R.id.et_room_num)

        cancelBtn.setOnClickListener {
            dismiss()
        }

        createBtn.setOnClickListener {
            var map: HashMap<String, Any> = HashMap()
            map.put("name", roomName.text.toString())
            map.put("num_player", numPlayer.text.toString().toInt())
            val call: Call<Void> = retrofitInterface.makeRoom(map)
            call.enqueue(object: Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.code() == 200){
                        Toast.makeText(context, "Created Room", Toast.LENGTH_LONG).show()
                        val intent = Intent(context, InnerRoomActivity::class.java).apply{
                            putExtra("roomName", roomName.text.toString())
                            putExtra("game_id", game_id)
                        }
                        startActivity(intent)
                    } else if(response.code() == 400){
                        Toast.makeText(context, "Room with equivalent name already exists!", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    dismiss()
                }
            })
        }
        return RootView
    }
}