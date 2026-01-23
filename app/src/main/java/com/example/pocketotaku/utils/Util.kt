package com.example.pocketotaku.utils

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