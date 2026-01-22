package com.example.pocketotaku.data.model

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("data")
    val data: List<AnimeDto>
)

data class SingleAnimeResponse(
    @SerializedName("data")
    val data: AnimeDto
)

data class AnimeDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("score") val score: Double?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("images") val images: AnimeImages,
    @SerializedName("trailer") val trailer: AnimeTrailer?,
    @SerializedName("genres") val genres: List<Genre>?
)

data class AnimeImages(
    @SerializedName("jpg") val jpg: ImageUrl
)

data class ImageUrl(
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("large_image_url") val largeImageUrl: String?
)

data class AnimeTrailer(
    @SerializedName("url") val url: String?,
    @SerializedName("embed_url") val embedUrl: String?
)

data class Genre(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("name") val name: String
)
