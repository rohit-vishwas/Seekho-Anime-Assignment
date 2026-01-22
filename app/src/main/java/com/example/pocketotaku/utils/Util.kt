package com.example.pocketotaku.utils

import com.example.pocketotaku.utils.Constants.YOUTUBE_URL

object Util {

    fun extractYoutubeVideoId(url: String?): String {
        if (url.isNullOrEmpty()) return ""
        return try {
            when {
                url.contains("/embed/") -> url.substringAfter("/embed/").substringBefore("?").substringBefore("&")
                url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
                url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?").substringBefore("&")
                else -> ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}