package com.example.logintest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ScoreFragment(var userName: String?, var score:Int?) : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var RootView = inflater.inflate(R.layout.fragment_score, container, false)
        var tv_userName = RootView.findViewById<TextView>(R.id.tv_username)
        var tv_score = RootView.findViewById<TextView>(R.id.tv_score)
        var tv_tier = RootView.findViewById<TextView>(R.id.tv_tier)
        var iv_tier = RootView.findViewById<ImageView>(R.id.iv_tier)
        var btn_close = RootView.findViewById<Button>(R.id.bt_close)
        tv_userName.text = userName
        tv_score.text = "점수: "+score.toString()
        if(score!! <0){
            tv_tier.text = "당신의 티어는: Bronze"
            iv_tier.setImageResource(R.drawable.bronze)
        } else if(score!! < 50){
            tv_tier.text = "당신의 티어는: Silver"
            iv_tier.setImageResource(R.drawable.silver)
        } else if(score!! < 100){
            tv_tier.text = "당신의 티어는: Gold"
            iv_tier.setImageResource(R.drawable.gold)
        } else if(score!! < 150){
            tv_tier.text = "당신의 티어는: Platinum"
            iv_tier.setImageResource(R.drawable.platinum)
        } else if(score!! < 200){
            tv_tier.text = "당신의 티어는: Diamond"
            iv_tier.setImageResource(R.drawable.diamond)
        } else {
            tv_tier.text = "당신의 티어는: Challenger"
            iv_tier.setImageResource(R.drawable.challenger)
        }

        btn_close.setOnClickListener {
            dismiss()
        }
        return RootView
    }
}