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
    @SerializedName("url") val url: String?,
    @SerializedName("images") val images: AnimeImages,
    @SerializedName("trailer") val trailer: AnimeTrailer?,
    @SerializedName("approved") val approved: Boolean?,
    @SerializedName("titles") val titles: List<Title>?,
    @SerializedName("title") val title: String,
    @SerializedName("title_english") val titleEnglish: String?,
    @SerializedName("title_japanese") val titleJapanese: String?,
    @SerializedName("title_synonyms") val titleSynonyms: List<String>?,
    @SerializedName("type") val type: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("airing") val airing: Boolean?,
    @SerializedName("aired") val aired: Aired?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("score") val score: Double?,
    @SerializedName("scored_by") val scoredBy: Int?,
    @SerializedName("rank") val rank: Int?,
    @SerializedName("popularity") val popularity: Int?,
    @SerializedName("members") val members: Int?,
    @SerializedName("favorites") val favorites: Int?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("background") val background: String?,
    @SerializedName("season") val season: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("broadcast") val broadcast: Broadcast?,
    @SerializedName("producers") val producers: List<SimpleEntity>?,
    @SerializedName("licensors") val licensors: List<SimpleEntity>?,
    @SerializedName("studios") val studios: List<SimpleEntity>?,
    @SerializedName("genres") val genres: List<Genre>?,
    @SerializedName("explicit_genres") val explicitGenres: List<Genre>?,
    @SerializedName("themes") val themes: List<Genre>?,
    @SerializedName("demographics") val demographics: List<Genre>?
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
    @SerializedName("type") val type: String?,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String?
)

data class Title(
    @SerializedName("type") val type: String?,
    @SerializedName("title") val title: String
)

data class Aired(
    @SerializedName("from") val from: String?,
    @SerializedName("to") val to: String?,
    @SerializedName("string") val string: String?
)

data class Broadcast(
    @SerializedName("day") val day: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("string") val string: String?
)

data class SimpleEntity(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("type") val type: String?,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String?
)
