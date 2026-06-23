package com.szmaou.miramal.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.model.UserAnime
import com.szmaou.miramal.presentation.components.AnimeCard
import com.szmaou.miramal.presentation.components.LoadingIndicator

@Composable
fun FavoriteScreen(
    onAnimeClick: (Int) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabs = listOf("Local", "MAL List")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = uiState.selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = uiState.selectedTab == index,
                    onClick = { viewModel.selectTab(index) },
                    text = { Text(title) }
                )
            }
        }

        when (uiState.selectedTab) {
            0 -> LocalFavoritesTab(
                favorites = uiState.localFavorites,
                isLoading = uiState.isLoadingLocal,
                onAnimeClick = onAnimeClick
            )
            1 -> MalListTab(
                userAnimeList = uiState.malList,
                isLoading = uiState.isLoadingMalList,
                error = uiState.malListError,
                statusFilter = uiState.malListStatusFilter,
                onStatusFilter = { viewModel.setMalListFilter(it) },
                onAnimeClick = onAnimeClick
            )
        }
    }
}

@Composable
private fun LocalFavoritesTab(
    favorites: List<Anime>,
    isLoading: Boolean,
    onAnimeClick: (Int) -> Unit
) {
    when {
        isLoading -> LoadingIndicator()
        favorites.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No favorites yet")
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(favorites, key = { it.malId }) { anime ->
                    AnimeCard(
                        anime = anime,
                        onClick = { onAnimeClick(anime.malId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MalListTab(
    userAnimeList: List<UserAnime>,
    isLoading: Boolean,
    error: String?,
    statusFilter: String?,
    onStatusFilter: (String?) -> Unit,
    onAnimeClick: (Int) -> Unit
) {
    val statusOptions = listOf(
        null to "All",
        "watching" to "Watching",
        "completed" to "Completed",
        "on_hold" to "On Hold",
        "dropped" to "Dropped",
        "plan_to_watch" to "Plan to Watch"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(statusOptions) { (status, label) ->
                FilterChip(
                    selected = statusFilter == status,
                    onClick = { onStatusFilter(status) },
                    label = { Text(label) }
                )
            }
        }

        when {
            isLoading -> LoadingIndicator()
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error)
                }
            }
            userAnimeList.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No anime in this list")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(userAnimeList, key = { it.anime.malId }) { userAnime ->
                        AnimeCard(
                            anime = userAnime.anime,
                            onClick = { onAnimeClick(userAnime.anime.malId) }
                        )
                    }
                }
            }
        }
    }
}
