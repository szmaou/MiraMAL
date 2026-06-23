package com.szmaou.miramal.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.model.UserAnime
import com.szmaou.miramal.domain.repository.UserRepository
import com.szmaou.miramal.domain.usecase.ManageFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteUiState(
    val selectedTab: Int = 0,
    val localFavorites: List<Anime> = emptyList(),
    val isLoadingLocal: Boolean = true,
    val malList: List<UserAnime> = emptyList(),
    val isLoadingMalList: Boolean = false,
    val malListError: String? = null,
    val malListStatusFilter: String? = null
)

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val manageFavoriteUseCase: ManageFavoriteUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    init {
        loadLocalFavorites()
    }

    private fun loadLocalFavorites() {
        viewModelScope.launch {
            manageFavoriteUseCase.getAllFavorites().collect { favorites ->
                _uiState.value = _uiState.value.copy(
                    localFavorites = favorites,
                    isLoadingLocal = false
                )
            }
        }
    }

    fun loadMalList(status: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingMalList = true,
                malListError = null,
                malListStatusFilter = status
            )
            userRepository.getUserAnimeList(status = status)
                .onSuccess { list ->
                    _uiState.value = _uiState.value.copy(
                        malList = list,
                        isLoadingMalList = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingMalList = false,
                        malListError = e.message ?: "Failed to load list"
                    )
                }
        }
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
        if (index == 1 && _uiState.value.malList.isEmpty()) {
            loadMalList()
        }
    }

    fun setMalListFilter(status: String?) {
        loadMalList(status)
    }
}
