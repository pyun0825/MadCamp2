package com.example.logintest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import org.json.JSONArray

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
        setContentView(R.layout.activity_game)

        val mSocket = SocketHandler.getSocket()

        val roomName = intent.getStringExtra("roomName")
        val my_game_id = intent.getStringExtra("game_id")
        var num_player = intent.getIntExtra("num_player", 0)
        var players: ArrayList<String> = ArrayList<String>()
        mSocket.emit("ready", roomName, num_player)

        mSocket.on("start") {
            mSocket.emit("enlist", my_game_id)
        }
        mSocket.on("initial turn"){ args->
            var jArray:JSONArray = args[0] as JSONArray
            if(jArray != null){
                for(i in 1 until jArray.length()){
                    players.add(jArray.getString(i))
                }
            }
            //ImageView에 플레이어 할당
            System.out.println("ImageView에 플레이어 할당")
            mSocket.on("turn"){ args->
                var fruit:Int = args[0] as Int
                var fruitNum:Int = args[1] as Int
                var turnPlayer: String = args[2] as String
                System.out.println("Fruit: ${fruit} Num: ${fruitNum} Player: ${turnPlayer}")
                // 해당 플레이어 카드 draw
                if(turnPlayer == my_game_id){
                    mSocket.emit("bell")
                }
            }
        }
        back_card = findViewById(R.id.iv_DeckView)
        front_card = findViewById(R.id.iv_myDeck)
        from_card = findViewById(R.id.iv_drawCard)
        to_card = findViewById(R.id.iv_myOpen)
        back_card2 = findViewById(R.id.iv_DeckView2)
        front_card2 = findViewById(R.id.iv_Deck2)
        from_card2 = findViewById(R.id.iv_drawCard2)
        to_card2 = findViewById(R.id.iv_Open2)
        back_card3 = findViewById(R.id.iv_DeckView3)
        front_card3 = findViewById(R.id.iv_Deck3)
        from_card3 = findViewById(R.id.iv_drawCard3)
        to_card3 = findViewById(R.id.iv_Open3)
        back_card4 = findViewById(R.id.iv_DeckView4)
        front_card4 = findViewById(R.id.iv_Deck4)
        from_card4 = findViewById(R.id.iv_drawCard4)
        to_card4 = findViewById(R.id.iv_Open4)
    }

    fun onRing(view: View) {
        if (started==1) {
            return
        }
        if (N < 4) {
            back_card4.visibility = View.INVISIBLE
            front_card4.visibility = View.INVISIBLE
            from_card4.visibility = View.INVISIBLE
            to_card4.visibility = View.INVISIBLE
            if (N == 2) {
                back_card3.visibility = View.INVISIBLE
                front_card3.visibility = View.INVISIBLE
                from_card3.visibility = View.INVISIBLE
                to_card3.visibility = View.INVISIBLE
                val layoutParams: LayoutParams = to_card2.getLayoutParams() as LayoutParams
                layoutParams.endToStart = R.id.rightLine
                to_card2.setLayoutParams(layoutParams)
            } else  {
                val layoutParams: LayoutParams = to_card3.getLayoutParams() as LayoutParams
                layoutParams.endToStart = R.id.rightLine
                to_card3.setLayoutParams(layoutParams)
            }
        }
        front_card.bringToFront()
        back_card.bringToFront()
        front_card2.bringToFront()
        back_card2.bringToFront()
        front_card3.bringToFront()
        back_card3.bringToFront()
        front_card4.bringToFront()
        back_card4.bringToFront()
        setBanim(banim, back_card, from_card, to_card, "Y")
        setFanim(fanim, front_card, from_card, to_card, "Y")
        setBanim(banim2, back_card2, from_card2, to_card2, "X")
        setFanim(fanim2, front_card2, from_card2, to_card2, "X")
        setBanim(banim3, back_card3, from_card3, to_card3, "X")
        setFanim(fanim3, front_card3, from_card3, to_card3, "X")
        setBanim(banim4, back_card4, from_card4, to_card4, "X")
        setFanim(fanim4, front_card4, from_card4, to_card4, "X")
        started = 1
    }
    fun onClickDraw(view: View) {
        Toast.makeText(this, "Player 1 Draw!", Toast.LENGTH_SHORT).show()
        fanim.start()
        banim.start()
    }

    fun onClickDraw2(view: View) {
        Toast.makeText(this, "Player 2 Draw!", Toast.LENGTH_SHORT).show()
        setBanim(banim2, back_card2, from_card2, to_card2, "X")
        setFanim(fanim2, front_card2, from_card2, to_card2, "X")
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

    fun setFanim(fanim: AnimatorSet, who: View, from: View, to: View, rot: String) {
        who.x = from.x
        who.y = from.y
        var dx: Float = to.x - from.x
        var dy: Float = to.y - from.y
        var fanim1: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 1f, 0f)
        fanim1.setDuration(0)
        var fanim2: ObjectAnimator = ObjectAnimator.ofFloat(who, "rotation$rot", -180f, 0f)
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

    fun setBanim(banim: AnimatorSet, who: View, from: View, to: View, rot: String) {
        who.x = from.x
        who.y = from.y
        var dx: Float = to.x - from.x
        var dy: Float = to.y - from.y
        var banim1: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 0f, 1f)
        banim1.setDuration(0)
        var banim2: ObjectAnimator = ObjectAnimator.ofFloat(who, "rotation$rot", 0f, 180f)
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