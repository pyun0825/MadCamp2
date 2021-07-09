package com.example.logintest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var retrofit: Retrofit
    lateinit var retrofitInterface: RetrofitInterface
    var BASE_URL:String = "http://143.248.226.140:3000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        findViewById<Button>(R.id.login).setOnClickListener {
            handleLoginDialog()
        }
        findViewById<Button>(R.id.signup).setOnClickListener {
            handleSignupDialog()
        }
    }

    private fun handleLoginDialog() {
        var view = layoutInflater.inflate(R.layout.login_dialog, null)
        var builder = AlertDialog.Builder(this)
        builder.setView(view)
        var ad = builder.create()
        ad.show()
        var loginBtn = view.findViewById<Button>(R.id.login)
        var emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        var passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)
        loginBtn.setOnClickListener {
            var map: HashMap<String,String> = HashMap()
            map.put("email", emailEdit.text.toString())
            map.put("password", passwordEdit.text.toString())
            var call: Call<LoginResult> = retrofitInterface.executeLogin(map)
            call.enqueue(object: Callback<LoginResult>{
                override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                    if(response.code()==200){
                        var result = response.body()

                        var builder1 = AlertDialog.Builder(this@MainActivity)
                        if (result != null) {
                            builder1.setTitle(result.name)
                        }
                        if (result != null) {
                            builder1.setMessage(result.email)
                        }

                        builder1.show()

                        val intent = Intent(this@MainActivity, RoomActivity::class.java)
                        startActivity(intent)

                    } else if(response.code() == 400){
                        Toast.makeText(this@MainActivity, "Wrong Credentials", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
            ad.dismiss()
        }
    }

    private fun handleSignupDialog() {
        var view = layoutInflater.inflate(R.layout.signup_dialog, null)
        var builder = AlertDialog.Builder(this)
        builder.setView(view)
        var ad = builder.create()
        ad.show()
        var signupBtn = view.findViewById<Button>(R.id.signup)
        var nameEdit = view.findViewById<EditText>(R.id.nameEdit)
        var emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        var passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)
        signupBtn.setOnClickListener {
            var map: HashMap<String,String> = HashMap()
            map.put("name", nameEdit.text.toString())
            map.put("email", emailEdit.text.toString())
            map.put("password", passwordEdit.text.toString())
            var call: Call<Void> = retrofitInterface.executeSignup(map)
            call.enqueue(object: Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.code() == 200){
                        Toast.makeText(this@MainActivity, "Signed up successfully", Toast.LENGTH_LONG).show()
                    } else if(response.code() == 400){
                        Toast.makeText(this@MainActivity, "Already registered", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
            ad.dismiss()
        }
    }
}