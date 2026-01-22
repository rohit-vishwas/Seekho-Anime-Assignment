package com.example.pocketotaku.data.repository

import com.example.pocketotaku.data.db.AnimeEntity
import com.example.pocketotaku.data.db.AnimeGenreCrossRef
import com.example.pocketotaku.data.db.GenreEntity
import com.example.pocketotaku.data.model.AnimeDto

object AnimeMapper {

    fun mapToEntities(dtos: List<AnimeDto>): Triple<List<AnimeEntity>, List<GenreEntity>, List<AnimeGenreCrossRef>> {
        val animeList = mutableListOf<AnimeEntity>()
        val genreList = mutableListOf<GenreEntity>()
        val crossRefs = mutableListOf<AnimeGenreCrossRef>()

        dtos.forEach { dto ->
            val anime = AnimeEntity(
                malId = dto.malId,
                title = dto.title,
                titleEnglish = dto.titleEnglish,
                titleJapanese = dto.titleJapanese,
                url = dto.url,
                type = dto.type,
                source = dto.source,
                episodes = dto.episodes,
                status = dto.status,
                airing = dto.airing,
                duration = dto.duration,
                rating = dto.rating,
                score = dto.score,
                rank = dto.rank,
                popularity = dto.popularity,
                members = dto.members,
                favorites = dto.favorites,
                synopsis = dto.synopsis,
                background = dto.background,
                season = dto.season,
                year = dto.year,
                broadcastString = dto.broadcast?.string,
                airedString = dto.aired?.string,
                posterUrl = dto.images.jpg.largeImageUrl ?: dto.images.jpg.imageUrl,
                trailerUrl = dto.trailer?.embedUrl ?: dto.trailer?.url
            )
            animeList.add(anime)

            dto.genres?.forEach { genreDto ->
                genreList.add(GenreEntity(genreDto.malId, genreDto.name))
                crossRefs.add(AnimeGenreCrossRef(dto.malId, genreDto.malId))
            }
        }

        return Triple(animeList, genreList.distinctBy { it.malId }, crossRefs) // Distinct genres
    }
}
