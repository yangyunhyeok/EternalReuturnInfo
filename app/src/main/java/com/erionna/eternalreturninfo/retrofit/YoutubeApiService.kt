package com.erionna.eternalreturninfo.retrofit

import com.erionna.eternalreturninfo.util.Constants.Companion.AUTH_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    @GET("search")
    suspend fun getYouTubeVideos(
        @Query("key")apiKey: String = AUTH_KEY,
        @Query("q")query: String,
        @Query("order")videoOrder: String,
        @Query("type")videoType: String = "video",
        @Query("maxResults")maxResults: Int,
        @Query("channelId")channelId: String = "",
        @Query("part")part: String = "snippet",
    ): Response<YoutubeVideo>

    @GET("search")
    suspend fun getYouTubeMoreVideos(
        @Query("key") apiKey: String = AUTH_KEY,
        @Query("q") query: String,
        @Query("pageToken") nextPageToken: String,
        @Query("order") videoOrder: String,
        @Query("type") videoType: String = "video",
        @Query("maxResults") maxResults: Int,
        @Query("channelId") channelId: String = "",
        @Query("part") part: String = "snippet",
    ): Response<YoutubeVideo>
}
