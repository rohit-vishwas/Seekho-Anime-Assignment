package com.example.pocketotaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pocketotaku.ui.navigation.NavGraph
import com.example.pocketotaku.ui.theme.PocketOtakuTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.pocketotaku.ui.component.ErrorToast
import com.example.pocketotaku.utils.GlobalErrorManager
import kotlinx.coroutines.delay

import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val globalErrorManager: GlobalErrorManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketOtakuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showToast by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        globalErrorManager.errors.collect {
                            showToast = true
                            delay(5000)
                            showToast = false
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        NavGraph()

                        AnimatedVisibility(
                            visible = showToast,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            ErrorToast()
                        }
                    }
                }
            }
        }
    }
}
