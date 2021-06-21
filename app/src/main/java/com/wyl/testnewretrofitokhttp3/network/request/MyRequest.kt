package com.wyl.testnewretrofitokhttp3.network.request

import com.example.myapplication.bean.ItemList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyRequest {

    @GET("/mianshi1.js")
    suspend fun getJson(): Call<ItemList>
}