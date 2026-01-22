package com.example.pocketotaku.data.db

import androidx.room.*

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey
    val malId: Int,
    val title: String,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val url: String?,
    val type: String?,
    val source: String?,
    val episodes: Int?,
    val status: String?,
    val airing: Boolean?,
    val duration: String?,
    val rating: String?,
    val score: Double?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val synopsis: String?,
    val background: String?,
    val season: String?,
    val year: Int?,
    val broadcastString: String?, // Storing as String to simplify
    val airedString: String?,     // Storing as String to simplify
    val posterUrl: String?,
    val trailerUrl: String?,
    val isFavorite: Boolean = false // User preference
)

@Entity(tableName = "genre_table")
data class GenreEntity(
    @PrimaryKey
    val malId: Int, // The genre's ID from API
    val name: String
)

@Entity(
    tableName = "anime_genre_cross_ref",
    primaryKeys = ["animeId", "genreId"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["malId"],
            childColumns = ["animeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreEntity::class,
            parentColumns = ["malId"],
            childColumns = ["genreId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["genreId"])]
)
data class AnimeGenreCrossRef(
    val animeId: Int,
    val genreId: Int
)

data class AnimeWithGenres(
    @Embedded val anime: AnimeEntity,
    @Relation(
        parentColumn = "malId",
        entityColumn = "malId",
        associateBy = Junction(
            value = AnimeGenreCrossRef::class,
            parentColumn = "animeId",
            entityColumn = "genreId"
        )
    )
    val genres: List<GenreEntity>
)
