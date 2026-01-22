package com.example.pocketotaku.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.pocketotaku.data.db.AnimeWithGenres
import com.example.pocketotaku.utils.Util.extractYoutubeVideoId
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun AnimeDetailContent(animeData: AnimeWithGenres) {
    val anime = animeData.anime
    val genres = animeData.genres

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (!anime.trailerUrl.isNullOrEmpty()) {
            val videoId = extractYoutubeVideoId(anime.trailerUrl)
            if (videoId.isNotEmpty()) {
                var isVideoPlaying by remember { mutableStateOf(false) }

                if (isVideoPlaying) {
                    val lifecycleOwner = LocalLifecycleOwner.current
                    AndroidView(
                        factory = { context ->
                            YouTubePlayerView(context).apply {
                                lifecycleOwner.lifecycle.addObserver(this)
                                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                        youTubePlayer.loadVideo(videoId, 0f)
                                    }
                                })
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(shape = RoundedCornerShape(16.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(shape = RoundedCornerShape(16.dp))
                            .clickable { isVideoPlaying = true },
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = anime.posterUrl ?: "",
                            contentDescription = "Play Video",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(modifier = Modifier.padding(2.dp).clip(shape = CircleShape).background(color = Color(0x66000000))) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                    }
                }
            } else {
                 Box(modifier = Modifier.height(250.dp).fillMaxWidth()) {
                    Text("Invalid Trailer URL")
                }
            }
        } else if (!anime.posterUrl.isNullOrEmpty()) {
            AsyncImage(
                model = anime.posterUrl,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                Text("No Image Available")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(anime.title, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Rating: ${anime.score} | Episodes: ${anime.episodes}")
        Text("Genres: ${genres.joinToString { it.name }}")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Synopsis", style = MaterialTheme.typography.titleMedium)
        Text(anime.synopsis ?: "No synopsis available.")
    }
}
