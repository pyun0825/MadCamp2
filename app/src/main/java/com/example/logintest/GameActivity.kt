package com.example.logintest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.graphics.drawable.toDrawable
import io.socket.client.Socket
import org.json.JSONArray
import java.lang.System.currentTimeMillis

class GameActivity : AppCompatActivity() {

    var fanim: AnimatorSet = AnimatorSet()
    var banim: AnimatorSet = AnimatorSet()
    var fanim2: AnimatorSet = AnimatorSet()
    var banim2: AnimatorSet = AnimatorSet()
    var fanim3: AnimatorSet = AnimatorSet()
    var banim3: AnimatorSet = AnimatorSet()
    var fanim4: AnimatorSet = AnimatorSet()
    var banim4: AnimatorSet = AnimatorSet()
    var roomName = ""
    var my_game_id = ""
    var started = 0
    var fruitNum = 0
    var fruit = 0
    var turnPlayer = ""
    var turn_i = 0
    var fastest = ""
    var start_time: Long = 0
    var dt: Long = 0
    var emitted: Int = 0
    var id_map = mutableMapOf<String, Int>()
    var card_list = intArrayOf(R.drawable.card_apple_1, R.drawable.card_apple_2, R.drawable.card_apple_3, R.drawable.card_apple_4, R.drawable.card_apple_5
        , R.drawable.card_avocado_1, R.drawable.card_avocado_2, R.drawable.card_avocado_3, R.drawable.card_avocado_4, R.drawable.card_avocado_5
        , R.drawable.card_grape_1, R.drawable.card_grape_2, R.drawable.card_grape_3, R.drawable.card_grape_4, R.drawable.card_grape_5
        , R.drawable.card_lemon_1, R.drawable.card_lemon_2, R.drawable.card_lemon_3, R.drawable.card_lemon_4, R.drawable.card_lemon_5)
    lateinit var back_card: ImageView
    lateinit var front_card: ImageView
    lateinit var to_card: ImageView
    lateinit var from_card: ImageView
    lateinit var back_card2: ImageView
    lateinit var front_card2: ImageView
    lateinit var to_card2: ImageView
    lateinit var from_card2: ImageView
    lateinit var back_card3: ImageView
    lateinit var front_card3: ImageView
    lateinit var to_card3: ImageView
    lateinit var from_card3: ImageView
    lateinit var back_card4: ImageView
    lateinit var front_card4: ImageView
    lateinit var to_card4: ImageView
    lateinit var from_card4: ImageView
    lateinit var BASE_URL: String
    lateinit var mSocket: Socket
    var drawid: Int = R.drawable.card_lemon_2
    var drawid2: Int = R.drawable.card_lemon_3
    var drawid3: Int = R.drawable.card_grape_5
    var drawid4: Int = R.drawable.card_avocado_2
    var N: Int = 3
    var is_five: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSocket = SocketHandler.getSocket()

        roomName = intent.getStringExtra("roomName") as String
        my_game_id = intent.getStringExtra("game_id") as String
        N = intent.getIntExtra("num_player", 0)
        var players: ArrayList<String> = ArrayList<String>()
        mSocket.emit("ready", roomName, N)

        mSocket.on("initial turn"){ args->
            var jArray:JSONArray = args[0] as JSONArray
            System.out.println(args)
            System.out.println(jArray)
            Log.i("initial turn : ", "done")
            if(jArray != null){
                for(i in 0 until jArray.length()){
                    players.add(jArray.getString(i))
                }
            }
            System.out.println(players)

            for (i in 0 until N) {
                id_map[players[i]] = i
            }
            val j:Int = id_map[my_game_id]!!
            for (i in 0 until N) {
                id_map[players[i]] = (i-j+N)%N
            }
            for (i in 0 until N) {
                id_map[players[i]] = (i-j+N)%N
            }

            //ImageView에 플레이어 할당
            System.out.println("ImageView에 플레이어 할당")
            mSocket.on("turn"){ args->
                if (started==0) {
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
                fruit = args[0] as Int
                fruitNum = args[1] as Int
                turnPlayer = args[2] as String
                is_five = args[3] as Int
                turn_i = id_map[turnPlayer] as Int
                emitted = 0
                System.out.println("Fruit: ${fruit} Num: ${fruitNum} Player: ${turnPlayer} Playid: ${turn_i}")
                runOnUiThread {
                    if (turn_i == 0) {
                        to_card.setImageResource(drawid)
                        drawid = card_list[fruit * 5 + fruitNum]
                        front_card.setImageResource(drawid)
                        fanim.start()
                        banim.start()
                    } else if (turn_i == 1) {
                        to_card2.setImageResource(drawid2)
                        drawid2 = card_list[fruit * 5 + fruitNum]
                        front_card2.setImageResource(drawid2)
                        fanim2.start()
                        banim2.start()
                    } else if (turn_i == 2) {
                        to_card3.setImageResource(drawid3)
                        drawid3 = card_list[fruit * 5 + fruitNum]
                        front_card3.setImageResource(drawid3)
                        fanim3.start()
                        banim3.start()
                    } else if (turn_i == 3) {
                        to_card4.setImageResource(drawid4)
                        drawid4 = card_list[fruit * 5 + fruitNum]
                        front_card4.setImageResource(drawid4)
                        fanim4.start()
                        banim4.start()
                    }
                }
                start_time = currentTimeMillis()


                // 해당 플레이어 카드 draw
//                if(turnPlayer == my_game_id){
//                    mSocket.emit("bell", my_game_id)
//                }
            }
            mSocket.on("turnend") { args ->
                runOnUiThread {
                    if (args[0] == null) {
                        Toast.makeText(this, "Nobody rang the bell.", Toast.LENGTH_SHORT).show()
                    } else {
                        fastest = args[0] as String
                        Toast.makeText(this, "Player ${fastest} was the fastest!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

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
        if (is_five==1) {
            dt = currentTimeMillis() - start_time
            if (dt<300) {
                wrongRing()
            }
            if (emitted == 0) {
                Toast.makeText(this, "Player ${my_game_id} rang the bell!", Toast.LENGTH_SHORT).show()
            }
            emitted = 1
            mSocket.emit("ringbell", roomName, my_game_id, currentTimeMillis() - start_time)
            is_five = 0
        } else {
            wrongRing()
        }
    }

    fun wrongRing() {

    }

    fun onClickDraw(view: View) {
        if (started==1) {Toast.makeText(this, "Player 1 Draw!", Toast.LENGTH_SHORT).show()}
        fanim.start()
        banim.start()
    }

    fun onClickDraw2(view: View) {
        if (started==1) {Toast.makeText(this, "Player 2 Draw!", Toast.LENGTH_SHORT).show()}
        fanim2.start()
        banim2.start()
    }

    fun onClickDraw3(view: View) {
        if (started==1) {Toast.makeText(this, "Player 3 Draw!", Toast.LENGTH_SHORT).show()}
        fanim3.start()
        banim3.start()
    }

    fun onClickDraw4(view: View) {
        if (started==1) {Toast.makeText(this, "Player 4 Draw!", Toast.LENGTH_SHORT).show()}
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