package com.example.logintest

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.KakaoSdk.init

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 네이티브 앱 키로 초기화
        KakaoSdk.init(this, "8c2084e5975bafeb7699f8c69c473ba3")
    }
}