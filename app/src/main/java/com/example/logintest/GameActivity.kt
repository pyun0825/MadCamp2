package com.example.logintest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ContentValues
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {

    var fanim: AnimatorSet = AnimatorSet()
    var banim: AnimatorSet = AnimatorSet()
    lateinit var back_card:View
    lateinit var front_card:View
    lateinit var to_card:View
    lateinit var draw_card:View

//    var dpi:Int = 0
//    var density:Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        back_card = findViewById(R.id.imageView7)
        front_card = findViewById(R.id.imageView8)
        to_card = findViewById(R.id.imageView9)
        draw_card = findViewById(R.id.imageView10)
    }
    fun onClickDraw(view: View) {
        front_card.bringToFront()
        back_card.bringToFront()
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
        setBanim(back_card, draw_card, to_card, "Y")
        setFanim(front_card, draw_card, to_card, "Y")

        fanim.start()
        banim.start()
    }

    fun setBanim(who:View, from:View, to:View, rot:String) {
        who.x = from.x
        who.y = from.y
        var dx:Float = to.x-from.x
        var dy:Float = to.y-from.y
        var banim1: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 0f, 1f)
        banim1.setDuration(0)
        var banim2: ObjectAnimator = ObjectAnimator.ofFloat(who, "rotation$rot", 0f, 180f)
        banim2.repeatMode= ValueAnimator.REVERSE
        banim2.setDuration(600)
        var banim3: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationX", 0f, dx)
        banim3.repeatMode= ValueAnimator.REVERSE
        banim3.setDuration(600)
        var banim4: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationY", 0f, dy)
        banim4.repeatMode= ValueAnimator.REVERSE
        banim4.setDuration(600)
        var banim5: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 1f, 0f)
        banim5.startDelay = 300
        banim5.setDuration(0)
        var banim6: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationX", dx, 0f)
        banim6.startDelay = 600
        banim6.setDuration(0)
        var banim7: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationY", dy, 0f)
        banim7.startDelay = 600
        banim7.setDuration(0)
        banim.play(banim1)
        banim.play(banim2).with(banim1)
        banim.play(banim3).with(banim2)
        banim.play(banim4).with(banim3)
        banim.play(banim5).with(banim4)
        banim.play(banim6).with(banim5)
        banim.play(banim7).with(banim6)
    }
    fun setFanim(who:View, from:View, to:View, rot:String) {
        who.x = from.x
        who.y = from.y
        var dx:Float = to.x-from.x
        var dy:Float = to.y-from.y
        var fanim1: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 1f, 0f)
        fanim1.setDuration(0)
        var fanim2: ObjectAnimator = ObjectAnimator.ofFloat(who, "rotation$rot", -180f, 0f)
        fanim2.repeatMode= ValueAnimator.REVERSE
        fanim2.setDuration(600)
        var fanim3: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationX", 0f, dx)
        fanim3.repeatMode= ValueAnimator.REVERSE
        fanim3.setDuration(600)
        var fanim4: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationY", 0f, dy)
        fanim4.repeatMode= ValueAnimator.REVERSE
        fanim4.setDuration(600)
        var fanim5: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 0f, 1f)
        fanim5.startDelay = 300
        fanim5.setDuration(0)
        fanim.play(fanim1)
        fanim.play(fanim2).with(fanim1)
        fanim.play(fanim3).with(fanim2)
        fanim.play(fanim4).with(fanim3)
        fanim.play(fanim5).with(fanim4)
    }
}