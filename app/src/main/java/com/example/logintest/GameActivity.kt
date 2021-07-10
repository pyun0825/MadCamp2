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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        var scale:Float = applicationContext.resources.displayMetrics.density
        fanim = AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animator) as AnimatorSet
        banim = AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animator) as AnimatorSet
        flip_card = findViewById(R.id.imageView8)
        draw_card = findViewById(R.id.imageView7)
        flip_card.bringToFront()
        draw_card.bringToFront()
    }

    fun onClickDraw(view: View) {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()

        fanim.setTarget(draw_card)
        fanim.start()
        banim.setTarget(flip_card)
        banim.start()

    }
}