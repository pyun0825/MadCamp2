package com.example.logintest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams

class GameActivity : AppCompatActivity() {

    var fanim: AnimatorSet = AnimatorSet()
    var banim: AnimatorSet = AnimatorSet()
    var fanim2: AnimatorSet = AnimatorSet()
    var banim2: AnimatorSet = AnimatorSet()
    var fanim3: AnimatorSet = AnimatorSet()
    var banim3: AnimatorSet = AnimatorSet()
    var fanim4: AnimatorSet = AnimatorSet()
    var banim4: AnimatorSet = AnimatorSet()
    var started:Int = 0
    lateinit var back_card: View
    lateinit var front_card: View
    lateinit var to_card: View
    lateinit var from_card: View
    lateinit var back_card2: View
    lateinit var front_card2: View
    lateinit var to_card2: View
    lateinit var from_card2: View
    lateinit var back_card3: View
    lateinit var front_card3: View
    lateinit var to_card3: View
    lateinit var from_card3: View
    lateinit var back_card4: View
    lateinit var front_card4: View
    lateinit var to_card4: View
    lateinit var from_card4: View
    var N: Int = 3
    lateinit var BASE_URL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mSocket = SocketHandler.getSocket()

        val roomName = intent.getStringExtra("roomName")
        val my_game_id = intent.getStringExtra("game_id")
        var num_player = intent.getIntExtra("num_player", 0)

        mSocket.emit("ready", roomName, num_player)

        mSocket.on("start") { arg ->
            num_player = arg[0] as Int

        }
        N = num_player

        if (N==2) {
            setContentView(R.layout.activity_game_2)
        } else if (N==3) {
            setContentView(R.layout.activity_game_3)
        } else if (N==4) {
            setContentView(R.layout.activity_game_4)
        }

        back_card = findViewById(R.id.iv_DeckView)!!
        front_card = findViewById(R.id.iv_myDeck)!!
        from_card = findViewById(R.id.iv_drawCard)!!
        to_card = findViewById(R.id.iv_myOpen)!!
        back_card2 = findViewById(R.id.iv_DeckView2)!!
        front_card2 = findViewById(R.id.iv_Deck2)!!
        from_card2 = findViewById(R.id.iv_drawCard2)!!
        to_card2 = findViewById(R.id.iv_Open2)!!
        if (N>2) {
            back_card3 = findViewById(R.id.iv_DeckView3)!!
            front_card3 = findViewById(R.id.iv_Deck3)!!
            from_card3 = findViewById(R.id.iv_drawCard3)!!
            to_card3 = findViewById(R.id.iv_Open3)!!
            if (N>3) {
                back_card4 = findViewById(R.id.iv_DeckView4)!!
                front_card4 = findViewById(R.id.iv_Deck4)!!
                from_card4 = findViewById(R.id.iv_drawCard4)!!
                to_card4 = findViewById(R.id.iv_Open4)!!
            }
        }
        front_card.bringToFront()
        back_card.bringToFront()
        front_card2.bringToFront()
        back_card2.bringToFront()
        if (N>2) {
            front_card3.bringToFront()
            back_card3.bringToFront()
            if (N>3) {
                front_card4.bringToFront()
                back_card4.bringToFront()
            }
        }
    }

    fun onRing(view: View) {
        if (started==1) {
            return
        }
        setanim(fanim, banim, front_card, back_card, from_card, to_card, "Y")
        if (N==4) {
            setanim(fanim2, banim2, front_card2, back_card2, from_card2, to_card2, "X")
            setanim(fanim3, banim3, front_card3, back_card3, from_card3, to_card3, "X")
            setanim(fanim4, banim4, front_card4, back_card4, from_card4, to_card4, "X")
        } else {
            setanim(fanim2, banim2, front_card2, back_card2, from_card2, to_card2, "Y", -1)
            if (N==3) {
                setanim(fanim3, banim3, front_card3, back_card3, from_card3, to_card3, "Y")
            }
        }
        started = 1
    }
    fun onClickDraw(view: View) {
        Toast.makeText(this, "Player 1 Draw!", Toast.LENGTH_SHORT).show()
        fanim.start()
        banim.start()
    }

    fun onClickDraw2(view: View) {
        Toast.makeText(this, "Player 2 Draw!", Toast.LENGTH_SHORT).show()
        fanim2.start()
        banim2.start()
    }

    fun onClickDraw3(view: View) {
        Toast.makeText(this, "Player 3 Draw!", Toast.LENGTH_SHORT).show()
        fanim3.start()
        banim3.start()
    }

    fun onClickDraw4(view: View) {
        Toast.makeText(this, "Player 4 Draw!", Toast.LENGTH_SHORT).show()
        fanim4.start()
        banim4.start()
    }

    fun setanim(fanim: AnimatorSet, banim: AnimatorSet, front: View, back: View, from: View, to: View, rot: String, dir: Int = 1) {
        setFanim(fanim, front, from, to, rot, dir)
        setBanim(banim, back, from, to, rot, dir)
    }

    fun setFanim(fanim: AnimatorSet, who: View, from: View, to: View, rot: String, dir: Int) {
        who.x = from.x
        who.y = from.y
        var dx: Float = to.x - from.x
        var dy: Float = to.y - from.y
        var fanim1: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 1f, 0f)
        fanim1.setDuration(0)
        var fanim2: ObjectAnimator = ObjectAnimator.ofFloat(who, "rotation$rot", dir*-180f, 0f)
        fanim2.repeatMode = ValueAnimator.REVERSE
        fanim2.setDuration(600)
        var fanim3: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationX", 0f, dx)
        fanim3.repeatMode = ValueAnimator.REVERSE
        fanim3.setDuration(600)
        var fanim4: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationY", 0f, dy)
        fanim4.repeatMode = ValueAnimator.REVERSE
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

    fun setBanim(banim: AnimatorSet, who: View, from: View, to: View, rot: String, dir: Int) {
        who.x = from.x
        who.y = from.y
        var dx: Float = to.x - from.x
        var dy: Float = to.y - from.y
        var banim1: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 0f, 1f)
        banim1.setDuration(0)
        var banim2: ObjectAnimator = ObjectAnimator.ofFloat(who, "rotation$rot", 0f, dir*180f)
        banim2.repeatMode = ValueAnimator.REVERSE
        banim2.setDuration(600)
        var banim3: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationX", 0f, dx)
        banim3.repeatMode = ValueAnimator.REVERSE
        banim3.setDuration(600)
        var banim4: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationY", 0f, dy)
        banim4.repeatMode = ValueAnimator.REVERSE
        banim4.setDuration(600)
        var banim5: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 1f, 0f)
        banim5.startDelay = 300
        banim5.setDuration(0)
        banim.play(banim1)
        banim.play(banim2).with(banim1)
        banim.play(banim3).with(banim2)
        banim.play(banim4).with(banim3)
        banim.play(banim5).with(banim4)
    }
}