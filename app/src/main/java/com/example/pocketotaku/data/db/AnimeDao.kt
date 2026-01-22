package com.example.pocketotaku.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnimeGenreCrossRefs(crossRef: List<AnimeGenreCrossRef>)

    @Transaction
    suspend fun insertAnimeWithGenres(
        animeList: List<AnimeEntity>,
        genres: List<GenreEntity>,
        crossRefs: List<AnimeGenreCrossRef>
    ) {
        insertAnimeList(animeList)
        insertGenres(genres)
        insertAnimeGenreCrossRefs(crossRefs)
    }

    @Transaction // Important for @Relation
    @Query("SELECT * FROM anime_table ORDER BY rank ASC")
    fun getAllAnime(): Flow<List<AnimeWithGenres>>

    @Transaction
    @Query("SELECT * FROM anime_table WHERE malId = :id")
    suspend fun getAnimeById(id: Int): AnimeWithGenres?
}
