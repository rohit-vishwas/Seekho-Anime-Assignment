package com.example.pocketotaku.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketotaku.data.db.AnimeWithGenres
import com.example.pocketotaku.data.repository.AnimeRepository
import com.example.pocketotaku.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val animeId: Int = checkNotNull(savedStateHandle["animeId"])

    init {
        loadAnime(animeId)
    }

    private fun loadAnime(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            when (val result = repository.getAnimeDetail(id)) {
                is Resource.Success -> {
                    // Result can be AnimeWithGenres or similar.
                    // Repository currently returns Any/fallback. 
                    // Let's assume it returns AnimeWithGenres (as it fetches from DB mostly)
                    if (result.data is AnimeWithGenres) {
                         _uiState.value = DetailUiState.Success(result.data)
                    } else {
                        // Handle Api Response case directly if DB failed or something
                         _uiState.value = DetailUiState.Error("Unexpected data format")
                    }
                }
                is Resource.Error -> {
                    _uiState.value = DetailUiState.Error(result.message ?: "Error")
                }
                is Resource.Loading -> { /* no-op */ }
            }
        }
    }
}

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val anime: AnimeWithGenres) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
