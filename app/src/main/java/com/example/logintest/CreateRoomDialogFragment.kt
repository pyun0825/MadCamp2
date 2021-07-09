package com.example.logintest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class CreateRoomDialogFragment : DialogFragment() {
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

        cancelBtn.setOnClickListener {
            dismiss()
        }

        createBtn.setOnClickListener {

        }
        return RootView
    }
}