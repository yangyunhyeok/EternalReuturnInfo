package com.erionna.eternalreturninfo.util

import com.erionna.eternalreturninfo.BuildConfig


class Constants {
    companion object {
        const val BASE_URL = BuildConfig.YOUTUBE_BASE_URL
        const val AUTH_KEY = BuildConfig.YOUTUBE_API_KEY // 호식
        const val EXTRA_ER_MODEL = "extra_er_model"
        const val EXTRA_ER_POSITION = "extra_er_position"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_TIME = "extra_time"

        const val MAIN_BASE_URL = BuildConfig.MAIN_BASE_URL
        const val MAIN_APIKEY = BuildConfig.MAIN_API_KEY

        const val ETERNAL_RETURN_BASE_URL = BuildConfig.NOTICE_BASE_URL
    }

}