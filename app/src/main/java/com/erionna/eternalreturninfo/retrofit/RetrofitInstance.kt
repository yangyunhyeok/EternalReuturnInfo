package com.erionna.eternalreturninfo.retrofit

import com.erionna.eternalreturninfo.util.Constants
import com.erionna.eternalreturninfo.util.Constants.Companion.BASE_URL
import com.erionna.eternalreturninfo.util.Constants.Companion.ETERNAL_RETURN_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    private val eternal_retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor(logger = HttpLoggingInterceptor.Logger.DEFAULT).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()

        Retrofit.Builder()
            .baseUrl(ETERNAL_RETURN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val eternal_api by lazy {
        eternal_retrofit.create(ApiService::class.java)
    }

    private val user_retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.MAIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val search_userID_api by lazy {
        user_retrofit.create(UserService::class.java)
    }

    val search_user_state_api by lazy {
        user_retrofit.create(UserStatsService::class.java)
    }

}
