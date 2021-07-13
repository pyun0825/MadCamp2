package com.example.logintest.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.logintest.InnerRoomActivity
import com.example.logintest.R
import com.example.logintest.RoomResult

class  WaitingRoomAdapter(var context: Context, rooms: List<RoomResult>, var game_id:String?): BaseAdapter() {
    var rooms = rooms
    override fun getCount(): Int {
        return rooms.size
    }

    override fun getItem(position: Int): Any {
        return "TEST STRING"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val roomRow = layoutInflater.inflate(R.layout.waiting_room_item, parent, false)
        val roomTitle = roomRow.findViewById<TextView>(R.id.tv_room)
        val roomTurns = roomRow.findViewById<TextView>(R.id.tv_room_turns)
        val roomPlayers = roomRow.findViewById<TextView>(R.id.tv_room_players)
        roomTitle.text = rooms[position].name
        roomTurns.text = "${rooms[position].turn.toString()} turns"
        roomPlayers.text = "${rooms[position].cur_player.toString()}/${rooms[position].num_player.toString()}"

        roomRow.setOnClickListener {
            val intent = Intent(context, InnerRoomActivity::class.java).apply{
                putExtra("roomName", rooms[position].name)
                putExtra("game_id", game_id)
            }
            startActivity(context, intent, null)
        }

        return roomRow
    }
}