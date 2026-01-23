package com.example.pocketotaku

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.example.pocketotaku.di.appModule

class PocketOtakuApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PocketOtakuApp)
            modules(appModule)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .crossfade(true)
            .build()
    }
}
