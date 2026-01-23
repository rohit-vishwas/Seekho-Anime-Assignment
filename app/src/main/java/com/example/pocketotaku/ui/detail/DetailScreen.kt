package com.example.pocketotaku.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent, // Let content background show (if we wanted full screen)
        topBar = {
             // We can use a simple Box to hold the back button to keep it minimal/transparent
             Box(modifier = Modifier.padding(8.dp)) {
                 IconButton(
                     onClick = onBackClick,
                     modifier = Modifier
                         .background(
                             color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.4f),
                             shape = androidx.compose.foundation.shape.CircleShape
                         )
                 ) {
                     Icon(
                         imageVector = Icons.Default.ArrowBack,
                         contentDescription = "Back",
                         tint = androidx.compose.ui.graphics.Color.White
                     )
                 }
             }
        }
    ) { padding ->
        // We ignore the top padding to let the video touch the status bar area or top edge
        // But we respect bottom padding
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            when (val uiState = state) {
                is DetailUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
                is DetailUiState.Success -> AnimeDetailContent(uiState.anime)
                is DetailUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Error: ${uiState.message}")
                }
            }
        }
    }
}
