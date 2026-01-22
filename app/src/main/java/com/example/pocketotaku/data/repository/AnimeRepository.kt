package com.example.pocketotaku.data.repository

import androidx.room.withTransaction
import com.example.pocketotaku.data.api.JikanApiService
import com.example.pocketotaku.data.db.AnimeDatabase
import com.example.pocketotaku.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val api: JikanApiService,
    private val db: AnimeDatabase
) {
    private val dao = db.animeDao()

    fun getTopAnime() = flow {
        emit(Resource.Loading())

        // 1. Emit cached data immediately
        // Note: collect and emit is a bit tricky with single source of truth in a simple flow
        // For a true "Single Source of Truth", we ideally observe DB. 
        // But here we do a simpler "fetch and save" trigger, and observe DB in UI or here.
        // Let's use the bound resource pattern logic: Query DB -> Emit -> Fetch -> Save -> Emit
        
        // This is a simplified approach using Flow<List> from DAO
        // We can't easily "emit" the flow itself inside this flow builder unless we use `emitAll`
        // But we want to fetch as well.
        
        try {
            val response = api.getTopAnime()
            val (animeEntities, genreEntities, crossRefs) = AnimeMapper.mapToEntities(response.data)

            db.withTransaction {
                dao.insertAnimeWithGenres(animeEntities, genreEntities, crossRefs)
            }
            // Logic: if fetch successful, DB is updated.
        } catch (e: Exception) {
             emit(Resource.Error(e.localizedMessage ?: "Process failed"))
            // If fetch fails, we still rely on DB. 
            // In this specific flow structure, maybe simpler to just expose DB Flow separately
            // and have a 'refresh' suspend function.
            // BUT, for a "Resource" based stream:
        }
        
        // Return DB source of truth
        // Since getTopAnime might be called repeatedly or we want to observe changes:
        // Ideally we return local data flow.
    }
    
    // Better Pattern for MVVM + Room + Offline:
    // Expose Flow<List<Anime>> from DB
    fun getAnimeListStream() = dao.getAllAnime()
    
    // Suspend function to refresh data
    suspend fun refreshAnimeList(): Resource<Unit> {
        return try {
            val response = api.getTopAnime()
            val (animeEntities, genreEntities, crossRefs) = AnimeMapper.mapToEntities(response.data)
            db.withTransaction {
                dao.insertAnimeWithGenres(animeEntities, genreEntities, crossRefs)
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getAnimeDetail(id: Int): Resource<Any> { // Returning Any for now, ideally Domain Model
        // Check DB
        val local = dao.getAnimeById(id)
        if (local != null) {
            return Resource.Success(local)
        }
        // Fetch if missing
        return try {
            val response = api.getAnimeDetails(id)
            // Just return it or save it?
            // "Store fetched anime data locally" - yes.
            // Only current anime? 
            // The API returns SingleAnimeResponse. 
            // We can reuse mapper for a single item (wrap in list)
            val (animeEntities, genreEntities, crossRefs) = AnimeMapper.mapToEntities(listOf(response.data))
            db.withTransaction {
                dao.insertAnimeWithGenres(animeEntities, genreEntities, crossRefs)
            }
             val newLocal = dao.getAnimeById(id)
             Resource.Success(newLocal ?: response.data) // Fallback to data
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error loading detail")
        }
    }
}
