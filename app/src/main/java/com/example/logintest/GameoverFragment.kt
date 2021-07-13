package com.example.logintest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class GameoverFragment(var place: Int, var result_id: String, var result_cardnum: String, var result_point: String) : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val RootView = inflater.inflate(R.layout.fragment_gameover, container, false)

        var gotowrBtn = RootView.findViewById<Button>(R.id.goto_wr)
        var tv_place = RootView.findViewById<TextView>(R.id.tv_place)
        var tv_result_id = RootView.findViewById<TextView>(R.id.tv_result_id)
        var tv_result_cardnum = RootView.findViewById<TextView>(R.id.tv_result_cardnum)
        var tv_result_point = RootView.findViewById<TextView>(R.id.tv_result_point)
        if (place == 1) {
            tv_place.setText("1st place")
        } else if (place == 2) {
            tv_place.setText("2nd place")
        } else if (place == 3) {
            tv_place.setText("3rd place")
        } else if (place == 4) {
            tv_place.setText("4th place")
        }
        tv_result_id.setText(result_id)
        tv_result_cardnum.setText(result_cardnum)
        tv_result_point.setText(result_point)
        this.isCancelable = false
        gotowrBtn.setOnClickListener {
            dismiss()
            activity?.finish()
        }
        return RootView
    }

    fun OnBackPressedCallback() {

    }
}