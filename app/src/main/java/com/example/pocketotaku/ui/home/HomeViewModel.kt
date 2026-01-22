package com.example.pocketotaku.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketotaku.data.repository.AnimeRepository
import com.example.pocketotaku.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = repository.getAnimeListStream()
        .map { list ->
            if (list.isEmpty()) {
                HomeUiState.Loading
            } else {
                HomeUiState.Success(list)
            }
        }
        .catch { e -> HomeUiState.Error(e.message ?: "Unknown error") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            repository.refreshAnimeList()
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val animeList: List<com.example.pocketotaku.data.db.AnimeWithGenres>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
