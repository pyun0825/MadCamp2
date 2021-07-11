package com.example.logintest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitInterface {
    @POST("/login")
    fun executeLogin(@Body map: HashMap<String, String>) : Call<LoginResult>

    @POST("/signup")
    fun executeSignup(@Body map: HashMap<String, String>) : Call<Void>

    @GET("/rooms")
    fun getRooms(): Call<List<RoomResult>>

    @POST("/rooms")
    fun makeRoom(@Body map: HashMap<String, Any>) : Call<Void>

    @POST("/update")
    fun updateRoom(@Body map: HashMap<String, String>): Call<EnterRoomResult>

    @POST("/enter")
    fun enterRoom(@Body map: HashMap<String, String>): Call<Void>

    @POST("/exit")
    fun exitRoom(@Body map: HashMap<String, String>): Call<Void>
}