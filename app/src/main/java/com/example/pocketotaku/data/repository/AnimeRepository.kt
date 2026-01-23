package com.example.pocketotaku.data.repository

import androidx.room.withTransaction
import com.example.pocketotaku.data.api.JikanApiService
import com.example.pocketotaku.data.db.AnimeDatabase
import com.example.pocketotaku.data.db.AnimeEntity
import com.example.pocketotaku.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

import com.example.pocketotaku.utils.GlobalErrorManager
import kotlinx.coroutines.flow.catch

class AnimeRepository @Inject constructor(
    private val api: JikanApiService,
    private val db: AnimeDatabase,
    private val globalErrorManager: GlobalErrorManager
) {
    private val dao = db.animeDao()

    fun getTopAnime(): Flow<Resource<List<AnimeEntity>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getTopAnime()
            val (animeEntities, genreEntities, crossRefs) = AnimeMapper.mapToEntities(response.data)

            db.withTransaction {
                dao.insertAnimeWithGenres(animeEntities, genreEntities, crossRefs)
            }
            emit(Resource.Success(animeEntities))
        } catch (e: Exception) {
            globalErrorManager.triggerError()
             emit(Resource.Error(e.localizedMessage ?: "Process failed"))
        }
    }
    



    fun getAnimeListStream() = dao.getAllAnime()
        .catch { e ->
            globalErrorManager.triggerError()
            emit(emptyList())
        }

    suspend fun refreshAnimeList(): Resource<Unit> {
        return try {
            val response = api.getTopAnime()
            val (animeEntities, genreEntities, crossRefs) = AnimeMapper.mapToEntities(response.data)
            db.withTransaction {
                dao.insertAnimeWithGenres(animeEntities, genreEntities, crossRefs)
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            globalErrorManager.triggerError()
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getAnimeDetail(id: Int): Resource<Any> {
        val local = dao.getAnimeById(id)
        if (local != null) {
            return Resource.Success(local)
        }
        return try {
            val response = api.getAnimeDetails(id)
            val (animeEntities, genreEntities, crossRefs) = AnimeMapper.mapToEntities(listOf(response.data))
            db.withTransaction {
                dao.insertAnimeWithGenres(animeEntities, genreEntities, crossRefs)
            }
             val newLocal = dao.getAnimeById(id)
             Resource.Success(newLocal ?: response.data)
        } catch (e: Exception) {
            globalErrorManager.triggerError()
            Resource.Error(e.localizedMessage ?: "Error loading detail")
        }
    }
}
