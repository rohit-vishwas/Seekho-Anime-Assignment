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
                synopsis = dto.synopsis,
                episodes = dto.episodes,
                score = dto.score,
                rank = 0,
                popularity = 0,
                posterUrl = dto.images.jpg.imageUrl,
                trailerUrl = dto.trailer?.url
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
