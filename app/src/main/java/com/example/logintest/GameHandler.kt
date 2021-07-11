//package com.example.logintest
//
//import android.view.View
//import java.util.logging.Handler
//
//class GameHandler : Handler() {
//
//    var card_list = intArrayOf(R.drawable.card_apple_1, R.drawable.card_apple_2, R.drawable.card_apple_3, R.drawable.card_apple_4, R.drawable.card_apple_5
//        , R.drawable.card_avocado_1, R.drawable.card_avocado_2, R.drawable.card_avocado_3, R.drawable.card_avocado_4, R.drawable.card_avocado_5
//        , R.drawable.card_grape_1, R.drawable.card_grape_2, R.drawable.card_grape_3, R.drawable.card_grape_4, R.drawable.card_grape_5
//        , R.drawable.card_lemon_1, R.drawable.card_lemon_2, R.drawable.card_lemon_3, R.drawable.card_lemon_4, R.drawable.card_lemon_5)
//
//    override fun handleView(v: View) {
//        front_card.setBackgroundResource(card_list[fruit * 5 + fruitNum])
//    }
//}