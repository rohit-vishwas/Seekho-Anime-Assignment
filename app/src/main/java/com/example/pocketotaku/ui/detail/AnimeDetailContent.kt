package com.example.pocketotaku.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pocketotaku.data.db.AnimeWithGenres


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
            YoutubeVideoPlayer(
                trailerUrl = anime.trailerUrl,
                posterUrl = anime.posterUrl
            )
        } else if (!anime.posterUrl.isNullOrEmpty()) {
            AsyncImage(
                model = anime.posterUrl,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(shape = RoundedCornerShape(16.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentAlignment = Alignment.Center
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
