package com.example.pocketotaku.di

import androidx.room.Room
import com.example.pocketotaku.data.api.JikanApiService
import com.example.pocketotaku.data.db.AnimeDatabase
import com.example.pocketotaku.data.repository.AnimeRepository
import com.example.pocketotaku.ui.detail.DetailViewModel
import com.example.pocketotaku.ui.home.HomeViewModel
import com.example.pocketotaku.utils.Constants
import com.example.pocketotaku.utils.GlobalErrorManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(JikanApiService::class.java)
    }

    single {
        Room.databaseBuilder(
            androidApplication(),
            AnimeDatabase::class.java,
            "anime_database"
        ).build()
    }

    single {
        get<AnimeDatabase>().animeDao()
    }

    single {
        GlobalErrorManager()
    }

    single {
        AnimeRepository(get(), get(), get())
    }

    viewModel {
        HomeViewModel(get(), androidApplication())
    }

    viewModel { (animeId: Int) ->
        DetailViewModel(get(), animeId)
    }
}
