package com.example.logintest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.System.currentTimeMillis

class GameActivity : AppCompatActivity() {
    var roomName = ""
    var my_game_id = ""
    var turn_num = 0
    var recent_turn_num = 0
    var fruitNum = 0
    var fruit = 0
    var turnPlayer = ""
    var turn_i = 0
    var fastest = ""
    var start_time: Long = 0
    var dt: Long = 0
    var emitted: Int = 0
    var basetime: Long = 300
    var id_map = mutableMapOf<String, Int>()
    var num_open_cards = intArrayOf(0, 0, 0, 0)
    var num_close_cards = intArrayOf(0, 0, 0, 0)
    var total_turn_num: Int = 0
    var drawid = intArrayOf(R.drawable.card_empty, R.drawable.card_empty, R.drawable.card_empty, R.drawable.card_empty)
    var card_list = intArrayOf(R.drawable.card_apple_1, R.drawable.card_apple_2, R.drawable.card_apple_3, R.drawable.card_apple_4, R.drawable.card_apple_5
        , R.drawable.card_avocado_1, R.drawable.card_avocado_2, R.drawable.card_avocado_3, R.drawable.card_avocado_4, R.drawable.card_avocado_5
        , R.drawable.card_grape_1, R.drawable.card_grape_2, R.drawable.card_grape_3, R.drawable.card_grape_4, R.drawable.card_grape_5
        , R.drawable.card_lemon_1, R.drawable.card_lemon_2, R.drawable.card_lemon_3, R.drawable.card_lemon_4, R.drawable.card_lemon_5)
    var deck_pos: MutableList<Pair<Float, Float>> = mutableListOf()
    var open_pos: MutableList<Pair<Float, Float>> = mutableListOf()
    var fanim: MutableList<AnimatorSet> = mutableListOf()
    var banim: MutableList<AnimatorSet> = mutableListOf()
    lateinit var tv_leftturn: TextView
    lateinit var back_card: MutableList<ImageView>
    lateinit var front_card: MutableList<ImageView>
    lateinit var to_card: MutableList<ImageView>
    lateinit var from_card: MutableList<ImageView>
    lateinit var tv_stat: MutableList<TextView>
    lateinit var BASE_URL: String
    lateinit var mSocket: Socket
    lateinit var players: ArrayList<String>
    var N: Int = 3
    var is_five: Int = 0
    lateinit var mediaPlayer1:MediaPlayer
    lateinit var mediaPlayer2:MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaPlayer1 = MediaPlayer.create(this, R.raw.yes_five)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.not_five)

        mSocket = SocketHandler.getSocket()

        roomName = intent.getStringExtra("roomName") as String
        my_game_id = intent.getStringExtra("game_id") as String
        N = intent.getIntExtra("num_player", 0)
        players = ArrayList<String>()
        mSocket.emit("ready", roomName, N)

        mSocket.on("initial turn"){ args->
            var jArray:JSONArray = args[0] as JSONArray
            System.out.println(args[1])
            var dArray:JSONObject = JSONObject(args[1] as String)
            total_turn_num = args[2] as Int
            System.out.println(dArray)
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
                id_map[players[i]] = (i - j + N) % N
            }
            initText(dArray)
            for (i in 0 until N) {
                fanim.add(AnimatorSet())
                banim.add(AnimatorSet())
            }
            for (i in 0 until N){
                deck_pos.add(Pair(from_card[i].x, from_card[i].y))
                open_pos.add(Pair(to_card[i].x, to_card[i].y))
            }
            setanim(fanim[0], banim[0], front_card[0], back_card[0], deck_pos[0], deck_pos[0], open_pos[0], "Y")
            if (N==4) {
                for (i in 1 until 4) {
                    setanim(fanim[i], banim[i], front_card[i], back_card[i], deck_pos[i], deck_pos[i], open_pos[i], "X")
                }
            } else {
                setanim(fanim[1], banim[1], front_card[1], back_card[1], deck_pos[1], deck_pos[1], open_pos[1], "Y", -1)
                if (N==3) {
                    setanim(fanim[2], banim[2], front_card[2], back_card[2], deck_pos[2], deck_pos[2], open_pos[2], "Y")
                }
            }
        }

        mSocket.on("turn"){ args->
            runOnUiThread {
                System.out.println("recent_turn_num, N : "+recent_turn_num+N)
                if (recent_turn_num>N) {
                    for (i in to_card) {i.visibility = View.VISIBLE}
                }
                for (i in front_card) {i.bringToFront()}
                for (i in back_card) {i.bringToFront()}
            }
            recent_turn_num++
            turn_num++
            fruit = args[0] as Int
            fruitNum = args[1] as Int
            turnPlayer = args[2] as String
            is_five = args[3] as Int
            turn_i = id_map[turnPlayer] as Int
            emitted = 0
            System.out.println("Fruit: ${fruit} Num: ${fruitNum} Player: ${turnPlayer} Playid: ${turn_i}")
            num_close_cards[turn_i]--
            num_open_cards[turn_i]++
            updateText()
            runOnUiThread {
                to_card[turn_i].setImageResource(drawid[turn_i])
                drawid[turn_i] = card_list[fruit * 5 + fruitNum]
                front_card[turn_i].setImageResource(drawid[turn_i])
                fanim[turn_i].start()
                banim[turn_i].start()
            }
            start_time = currentTimeMillis()


            // 해당 플레이어 카드 draw
//                if(turnPlayer == my_game_id){
//                    mSocket.emit("bell", my_game_id)
//                }
        }
        mSocket.on("turnend") { args ->
            recent_turn_num = 0
            runOnUiThread {
                if (args[0] == null) {
//                        Toast.makeText(this, "Nobody rang the bell.", Toast.LENGTH_SHORT).show()
                } else {
                    fastest = args[0] as String
                    Toast.makeText(this, "Player ${fastest} was the fastest!", Toast.LENGTH_SHORT).show()
                    var j:Int = id_map[fastest]!!
                    var flipfanim: AnimatorSet = AnimatorSet()
                    var flipbanim: AnimatorSet = AnimatorSet()
                    var movefanim: AnimatorSet = AnimatorSet()
                    var movebanim: AnimatorSet = AnimatorSet()
                    for (i in 0 until N) {
//                            back_card[i].x = to_card[i].x
//                            back_card[i].y = to_card[i].y
//                            front_card[i].x = to_card[i].x
//                            front_card[i].y = to_card[i].y
                        if (num_open_cards[i] > 0) {
                            setanim(movefanim, movebanim, back_card[i], front_card[i], deck_pos[i], open_pos[i], open_pos[i], "Y", dur = 100)
                            setanim(flipfanim, flipbanim, back_card[i], front_card[i], deck_pos[i], open_pos[i], deck_pos[j], "", dur = 100, del = 200)
                        }
                    }
                    for (i in 0 until N) {
                        num_close_cards[j] += num_open_cards[i]
                        num_open_cards[i] = 0
                    }
                    from_card[j].bringToFront()
                    for (i in to_card) {i.visibility = View.INVISIBLE}
                    movefanim.start()
                    movebanim.start()
                    flipfanim.start()
                    flipbanim.start()
                    updateText()
                }
            }
        }

        mSocket.on("game over") { args ->
            var loser = args[0] as String
            var my_cards = num_close_cards[id_map[my_game_id]!!]
            var resultstr = ""
            var place = 1
            for (i in players) {
                if (num_close_cards[id_map[i]!!] > my_cards) {
                    place++
                }
                resultstr += "${i} : ${num_close_cards[id_map[i]!!]}\n"
            }
            if (my_game_id == loser) {
                resultstr += "Lost 10 points..."
            } else {
                resultstr += "Gained 10 points!"
            }
            val dialog = GameoverFragment(place, resultstr)
            dialog.show(supportFragmentManager, "GameOverDialog")
        }

        if (N==2) {
            setContentView(R.layout.activity_game_2)
        } else if (N==3) {
            setContentView(R.layout.activity_game_3)
        } else if (N==4) {
            setContentView(R.layout.activity_game_4)
        }

        tv_leftturn = findViewById(R.id.tv_leftturn)
        back_card = mutableListOf(findViewById(R.id.iv_DeckView)!!, findViewById(R.id.iv_DeckView2)!!)
        front_card = mutableListOf(findViewById(R.id.iv_myDeck)!!, findViewById(R.id.iv_Deck2)!!)
        from_card = mutableListOf(findViewById(R.id.iv_drawCard)!!, findViewById(R.id.iv_drawCard2)!!)
        to_card = mutableListOf(findViewById(R.id.iv_myOpen)!!, findViewById(R.id.iv_Open2)!!)
        tv_stat = mutableListOf(findViewById(R.id.tv_stat)!!, findViewById(R.id.tv_stat2)!!)
        if (N>2) {
            back_card.add(findViewById(R.id.iv_DeckView3)!!)
            front_card.add(findViewById(R.id.iv_Deck3)!!)
            from_card.add(findViewById(R.id.iv_drawCard3)!!)
            to_card.add(findViewById(R.id.iv_Open3)!!)
            tv_stat.add(findViewById(R.id.tv_stat3)!!)
            if (N>3) {
                back_card.add(findViewById(R.id.iv_DeckView4)!!)
                front_card.add(findViewById(R.id.iv_Deck4)!!)
                from_card.add(findViewById(R.id.iv_drawCard4)!!)
                to_card.add(findViewById(R.id.iv_Open4)!!)
                tv_stat.add(findViewById(R.id.tv_stat4)!!)
            }
        }
        for (i in to_card) {i.visibility = View.INVISIBLE}
        for (i in front_card) {i.bringToFront()}
        for (i in back_card) {i.bringToFront()}
    }


    fun onRing(view: View) {
        if (is_five==1) {
            dt = currentTimeMillis() - start_time
            if (dt<basetime) {
                wrongRing()
            }
            if (emitted == 0) {
                Toast.makeText(this, "Player ${my_game_id} rang the bell!", Toast.LENGTH_SHORT).show()
            }
            emitted = 1
            mSocket.emit("ringbell", roomName, my_game_id, currentTimeMillis() - start_time)
            is_five = 0
            mediaPlayer1.start()
        } else {
            wrongRing()
            mediaPlayer2.start()
        }
    }

    fun wrongRing() {

    }

    fun onClickDraw(view: View) {
        onClickDrawAll(0)
    }

    fun onClickDraw2(view: View) {
        onClickDrawAll(1)
    }

    fun onClickDraw3(view: View) {
        onClickDrawAll(2)
    }

    fun onClickDraw4(view: View) {
        onClickDrawAll(3)
    }

    fun onClickDrawAll(i: Int) {
//        if (recent_turn_num==1) {Toast.makeText(this, "Player ${i+1} Draw!", Toast.LENGTH_SHORT).show()}
//        fanim[i].start()
//        banim[i].start()
    }

    fun setanim(fanim: AnimatorSet, banim: AnimatorSet, front: View, back: View, ori: Pair<Float, Float>, from: Pair<Float, Float>, to: Pair<Float, Float>, rot: String, dir: Int = 1, dur : Long = basetime, del: Long = 0) {
        setFanim(fanim, front, ori, from, to, rot, dir, dur, del)
        setBanim(banim, back, ori, from, to, rot, dir, dur, del)
    }

    fun setFanim(fanim: AnimatorSet, who: View, ori: Pair<Float, Float>, from: Pair<Float, Float>, to: Pair<Float, Float>, rot: String, dir: Int, dur: Long, del: Long) {
        var fanim3: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationX", from.first - ori.first, to.first - ori.first)
        fanim3.repeatMode = ValueAnimator.REVERSE
        fanim3.startDelay = del
        fanim3.setDuration(2*dur)
        var fanim4: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationY", from.second - ori.second, to.second - ori.second)
        fanim4.repeatMode = ValueAnimator.REVERSE
        fanim4.startDelay = del
        fanim4.setDuration(2*dur)
        fanim.play(fanim3)
        fanim.play(fanim4).with(fanim3)
        if (rot != "") {
            var fanim1: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 1f, 0f)
            fanim1.startDelay = del
            fanim1.setDuration(0)
            var fanim2 = ObjectAnimator()
            fanim2 = ObjectAnimator.ofFloat(who, "rotation$rot", dir * -180f, 0f)
            fanim2.repeatMode = ValueAnimator.REVERSE
            fanim2.startDelay = del
            fanim2.setDuration(2 * dur)
            var fanim5: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 0f, 1f)
            fanim5.startDelay = dur + del
            fanim5.setDuration(0)
            fanim.play(fanim1).with(fanim3)
            fanim.play(fanim2).with(fanim3)
            fanim.play(fanim5).with(fanim3)}
    }

    fun setBanim(banim: AnimatorSet, who: View, ori: Pair<Float, Float>, from: Pair<Float, Float>, to: Pair<Float, Float>, rot: String, dir: Int, dur: Long, del: Long) {
        var banim3: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationX", from.first - ori.first, to.first - ori.first)
        banim3.repeatMode = ValueAnimator.REVERSE
        banim3.startDelay = del
        banim3.setDuration(2*dur)
        var banim4: ObjectAnimator = ObjectAnimator.ofFloat(who, "translationY", from.second - ori.second, to.second - ori.second)
        banim4.repeatMode = ValueAnimator.REVERSE
        banim4.startDelay = del
        banim4.setDuration(2*dur)
        banim.play(banim3)
        banim.play(banim4).with(banim3)
        if (rot != "") {
            var banim1: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 0f, 1f)
            banim1.startDelay = del
            banim1.setDuration(0)
            var banim2 = ObjectAnimator()
            banim2 = ObjectAnimator.ofFloat(who, "rotation$rot", 0f, dir * 180f)
            banim2.repeatMode = ValueAnimator.REVERSE
            banim2.startDelay = del
            banim2.setDuration(2 * dur)
            var banim5: ObjectAnimator = ObjectAnimator.ofFloat(who, "alpha", 1f, 0f)
            banim5.startDelay = dur + del
            banim5.setDuration(0)
            banim.play(banim1).with(banim3)
            banim.play(banim2).with(banim3)
            banim.play(banim5).with(banim3)}
    }

    fun initText(dArray: JSONObject) {
        for (i in players) {
            var j:Int = id_map[i]!!
            if (j==0) {
                System.out.println(dArray)
                num_close_cards[0] = dArray.getJSONObject(i).getInt("notOpen")
            } else if (j==1) {
                num_close_cards[1] = dArray.getJSONObject(i).getInt("notOpen")
            } else if (j==2) {
                num_close_cards[2] = dArray.getJSONObject(i).getInt("notOpen")
            } else if (j==3) {
                num_close_cards[3] = dArray.getJSONObject(i).getInt("notOpen")
            }
        }
        updateText()
    }

    fun updateText() {
        runOnUiThread {
            for (i in players) {
                var j: Int = id_map[i]!!
                tv_stat[j].setText("ID: ${i}\nCARDS: ${num_close_cards[j]}")
            }
            tv_leftturn.setText((total_turn_num - turn_num).toString())
        }
    }
}