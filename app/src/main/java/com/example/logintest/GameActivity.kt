package com.example.logintest

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    lateinit var fanim: AnimatorSet
    lateinit var banim: AnimatorSet
    lateinit var draw_card:View
    lateinit var flip_card:View
    lateinit var BASE_URL:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        BASE_URL = getString(R.string.ip_address)

        SocketHandler.setSocket(BASE_URL)
        val mSocket = SocketHandler.getSocket()

        mSocket.connect()

        val roomName = intent.getStringExtra("roomName")
        val my_game_id = intent.getStringExtra("game_id")
        val num_player = intent.getIntExtra("num_player", 0)

        mSocket.emit("ready", roomName, my_game_id, num_player)

        var scale:Float = applicationContext.resources.displayMetrics.density
        fanim = AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animator) as AnimatorSet
        banim = AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animator) as AnimatorSet
        flip_card = findViewById(R.id.iv_myDeck)
        draw_card = findViewById(R.id.iv_DeckView)
        flip_card.bringToFront()
        draw_card.bringToFront()

        mSocket.on("test"){
            System.out.println("Working!!!!!!!!!")
        }
    }

    fun onClickDraw(view: View) {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()

        fanim.setTarget(draw_card)
        fanim.start()
        banim.setTarget(flip_card)
        banim.start()

    }
}