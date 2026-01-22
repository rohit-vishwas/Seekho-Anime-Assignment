package com.example.pocketotaku.data.api

import com.example.pocketotaku.data.model.AnimeResponse
import com.example.pocketotaku.data.model.SingleAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApiService {

    @GET("top/anime")
    suspend fun getTopAnime(): AnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(@Path("id") id: Int): SingleAnimeResponse
}
