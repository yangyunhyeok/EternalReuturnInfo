package com.erionna.eternalreturninfo.retrofit

import com.google.gson.annotations.SerializedName

data class YoutubeVideo(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("nextPageToken")
    val nextPageToken: String,
    @SerializedName("regionCode")
    val regionCode: String,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    @SerializedName("items")
    val items: List<Items>?,
)


data class PageInfo(
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int
)

data class Items(
    @SerializedName("id")
    val id: Id,
    @SerializedName("snippet")
    val snippet: Snippet
)

data class Id(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("videoId")
    val videoId: String
)

data class Snippet(
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnails")
    val thumbnails: ThumbNail,
    @SerializedName("publishTime")
    val publishTime: String,
    @SerializedName("channelTitle")
    val channelTitle: String,
)

data class ThumbNail(
    @SerializedName("medium")
    val medium: Medium
)

data class Medium(
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)

data class YoutubeVideoInfo(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<TrendItem>?,
    @SerializedName("nextPageToken")
    val nextPageToken: String?
)

data class TrendItem(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("snippet")
    val snippet: Snippet,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("contentDetails")
    val contentDetails: ContentDetails,
    @SerializedName("statistics")
    val statistics: Statistics
)

data class ContentDetails(
    @SerializedName("duration")
    val duration: String
)

data class Statistics(
    @SerializedName("viewCount")
    val viewCount: String? = ""
)
