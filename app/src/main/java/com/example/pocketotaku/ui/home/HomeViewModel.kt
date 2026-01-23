package com.example.pocketotaku.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketotaku.data.repository.AnimeRepository
import com.example.pocketotaku.utils.Resource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.app.Application
import com.example.pocketotaku.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class HomeViewModel (
    private val repository: AnimeRepository,
    private val application: Application
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _errorState = MutableStateFlow<HomeUiState?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAnimeListStream(),
        _errorState,
        _isRefreshing
    ) { list, errorState, isRefreshing ->
        when {
            isRefreshing && list.isEmpty() -> HomeUiState.Loading
            list.isNotEmpty() -> HomeUiState.Success(list)
            errorState != null -> errorState
            else -> HomeUiState.Loading
        }
    }
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
            if (!repository.isDatabaseEmpty()) return@launch

            _errorState.value = null
            
            // If DB is empty, we need to check internet first
            val currentList = uiState.value
            val isListEmpty = currentList !is HomeUiState.Success || currentList.animeList.isEmpty()

            if (isListEmpty) {
                 if (!NetworkUtils.isInternetAvailable(application)) {
                    _errorState.value = HomeUiState.NoInternet
                    return@launch
                }
                _isRefreshing.value = true
            }

            val result = repository.refreshAnimeList()
            _isRefreshing.value = false

            if (result is Resource.Error && isListEmpty) {
                 _errorState.value = HomeUiState.EmptyError
            }
        }
    }
    
    fun retry() {
        refresh()
    }

    private val _navigationEvent = Channel<Int>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onAnimeClick(id: Int) {
        viewModelScope.launch {
            val result = repository.getAnimeDetail(id)
            if (result is Resource.Success) {
                _navigationEvent.send(id)
            }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val animeList: List<com.example.pocketotaku.data.db.AnimeWithGenres>) : HomeUiState
    data object EmptyError : HomeUiState
    data object NoInternet : HomeUiState
    data class Error(val message: String) : HomeUiState
}
