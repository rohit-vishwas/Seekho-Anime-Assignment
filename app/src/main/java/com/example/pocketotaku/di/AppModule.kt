package com.example.pocketotaku.di

import android.app.Application
import androidx.room.Room
import com.example.pocketotaku.data.api.JikanApiService
import com.example.pocketotaku.data.db.AnimeDatabase
import com.example.pocketotaku.utils.Constants
import com.example.pocketotaku.utils.GlobalErrorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideJikanApiService(retrofit: Retrofit): JikanApiService {
        return retrofit.create(JikanApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnimeDatabase(app: Application): AnimeDatabase {
        return Room.databaseBuilder(
            app,
            AnimeDatabase::class.java,
            "anime_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAnimeDao(db: AnimeDatabase) = db.animeDao()

    @Provides
    @Singleton
    fun provideGlobalErrorManager(): GlobalErrorManager {
        return GlobalErrorManager()
    }
}
