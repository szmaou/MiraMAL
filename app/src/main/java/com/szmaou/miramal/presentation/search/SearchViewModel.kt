package com.szmaou.miramal.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.usecase.SearchAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val results: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchAnimeUseCase: SearchAnimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun search() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                results = emptyList(),
                currentPage = 1
            )

            searchAnimeUseCase(query)
                .onSuccess { animeList ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        results = animeList,
                        currentPage = 1,
                        hasNextPage = animeList.size >= 25
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Search failed"
                    )
                }
        }
    }

    fun loadNextPage() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty() || _uiState.value.isLoading) return

        val nextPage = _uiState.value.currentPage + 1
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            searchAnimeUseCase(query, nextPage)
                .onSuccess { animeList ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        results = _uiState.value.results + animeList,
                        currentPage = nextPage,
                        hasNextPage = animeList.size >= 25
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
    }
}
