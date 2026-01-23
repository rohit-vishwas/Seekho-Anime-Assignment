package com.example.pocketotaku.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.pocketotaku.utils.NetworkUtils
import com.example.pocketotaku.utils.Util.extractYoutubeVideoId
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YoutubeVideoPlayer(
    trailerUrl: String,
    posterUrl: String?,
    modifier: Modifier = Modifier
) {
    val videoId = extractYoutubeVideoId(trailerUrl)
    if (videoId.isNotEmpty()) {
        var isVideoPlaying by remember { mutableStateOf(false) }
        var isPlayerReady by remember { mutableStateOf(false) }
        var playerError by remember { mutableStateOf<String?>(null) }
        val context = LocalContext.current

        fun onPlayClick() {
            if (NetworkUtils.isInternetAvailable(context)) {
                isVideoPlaying = true
                playerError = null
            } else {
                playerError = "No internet connection."
            }
        }

        fun onRetry() {
            if (NetworkUtils.isInternetAvailable(context)) {
                playerError = null
                isVideoPlaying = true
            } else {
                playerError = "No internet connection."
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentAlignment = Alignment.Center
        ) {
            if (isVideoPlaying) {
                val lifecycleOwner = LocalLifecycleOwner.current
                AndroidView(
                    factory = { context ->
                        YouTubePlayerView(context).apply {
                            lifecycleOwner.lifecycle.addObserver(this)
                            enableAutomaticInitialization = false

                            val options = IFramePlayerOptions.Builder(context = context)
                                .controls(1)
                                .rel(0)
                                .ivLoadPolicy(3)
                                .ccLoadPolicy(0)
                                .build()

                            initialize(object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.loadVideo(videoId, 0f)
                                    isPlayerReady = true
                                }

                                override fun onError(
                                    youTubePlayer: YouTubePlayer,
                                    error: PlayerConstants.PlayerError
                                ) {
                                    super.onError(youTubePlayer, error)
                                    isVideoPlaying = false
                                    playerError = "Something went wrong"
                                }
                            }, options)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (!isPlayerReady && playerError == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onPlayClick() },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = posterUrl ?: "",
                        contentDescription = "Play Video",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .background(color = Color(0x66000000))
                            .padding(8.dp)
                    ) {
                        if (isVideoPlaying) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }

            playerError?.let {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onPlayClick() },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = posterUrl ?: "",
                        contentDescription = "Play Video",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color(0x8B000000))
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = it,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onRetry() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Retry")
                        }
                    }

                }
            }
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentAlignment = Alignment.Center
        ) {
            Text("Invalid Trailer URL")
        }
    }
}