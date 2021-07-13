package com.example.logintest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

class SplashActivity : Activity() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var splash: ImageView = findViewById(R.id.iv_splash)
        splash.bringToFront()
        splash.setImageResource(R.drawable.splash_avocado)
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