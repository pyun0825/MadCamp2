package com.example.logintest

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class SplashActivity : Activity() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var splash: ImageView = findViewById(R.id.iv_splash)
        var cl_splash: ConstraintLayout = findViewById(R.id.cl_splash)
        var i: Int = (Math.random()*4).toInt()
        if (i==0) {
            cl_splash.setBackgroundColor(Color.parseColor("#84438f"))
            splash.setImageResource(R.drawable.splash_grape)
        } else if (i==1) {
            cl_splash.setBackgroundColor(Color.parseColor("#70ad47"))
            splash.setImageResource(R.drawable.splash_avocado)
        } else if (i==2) {
            cl_splash.setBackgroundColor(Color.parseColor("#d93d3d"))
            splash.setImageResource(R.drawable.splash_apple)
        } else if (i==3) {
            cl_splash.setBackgroundColor(Color.parseColor("#f9e049"))
            splash.setImageResource(R.drawable.splash_lemon)
        }
        splash.bringToFront()
        val hd = Handler()
        hd.postDelayed(splashhandler(), 1000) // 1초 후에 hd handler 실행  3000ms = 3초
    }

    private inner class splashhandler : Runnable {
        override fun run() {
            startActivity(
                Intent(
                    getApplication(),
                    MainActivity::class.java
                )
            ) //로딩이 끝난 후, ChoiceFunction 이동
            this@SplashActivity.finish() // 로딩페이지 Activity stack에서 제거
        }
    }

    override fun onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}