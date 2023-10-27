package com.erionna.eternalreturninfo.retrofit

import com.erionna.eternalreturninfo.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : YoutubeApiService by lazy {
        retrofit.create(YoutubeApiService::class.java)
    }
}