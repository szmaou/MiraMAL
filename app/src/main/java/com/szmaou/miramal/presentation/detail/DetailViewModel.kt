package com.szmaou.miramal.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.repository.UserRepository
import com.szmaou.miramal.domain.usecase.GetAnimeDetailUseCase
import com.szmaou.miramal.domain.usecase.ManageFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val anime: Anime? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val malListStatus: String? = null,
    val isMalListUpdating: Boolean = false,
    val malStatusError: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val manageFavoriteUseCase: ManageFavoriteUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val animeId: Int = savedStateHandle["animeId"] ?: -1

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
        observeFavoriteStatus()
        loadMalListStatus()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getAnimeDetailUseCase(animeId)
                .onSuccess { anime ->
                    _uiState.value = _uiState.value.copy(
                        anime = anime,
                        isLoading = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load detail"
                    )
                }
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            manageFavoriteUseCase.isFavorite(animeId).collect { isFav ->
                _uiState.value = _uiState.value.copy(isFavorite = isFav)
            }
        }
    }

    private fun loadMalListStatus() {
        viewModelScope.launch {
            userRepository.getUserAnimeList(offset = 0).onSuccess { list ->
                val status = list.find { it.anime.malId == animeId }?.listStatus?.status
                _uiState.value = _uiState.value.copy(malListStatus = status)
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val anime = _uiState.value.anime ?: return@launch
            if (_uiState.value.isFavorite) {
                manageFavoriteUseCase.removeFavorite(animeId)
            } else {
                manageFavoriteUseCase.addFavorite(anime)
            }
        }
    }

    fun updateMalListStatus(status: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isMalListUpdating = true,
                malStatusError = null
            )
            userRepository.updateAnimeListStatus(animeId, status)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        malListStatus = status,
                        isMalListUpdating = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isMalListUpdating = false,
                        malStatusError = e.message ?: "Failed to update status"
                    )
                }
        }
    }

    fun removeFromMalList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isMalListUpdating = true,
                malStatusError = null
            )
            userRepository.deleteAnimeFromList(animeId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        malListStatus = null,
                        isMalListUpdating = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isMalListUpdating = false,
                        malStatusError = e.message ?: "Failed to remove"
                    )
                }
        }
    }
}
