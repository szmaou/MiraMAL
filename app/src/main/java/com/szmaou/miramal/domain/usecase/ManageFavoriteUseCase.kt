package com.szmaou.miramal.domain.usecase

import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    fun getAllFavorites(): Flow<List<Anime>> = repository.getAllFavorites()

    fun isFavorite(animeId: Int): Flow<Boolean> = repository.isFavorite(animeId)

    suspend fun addFavorite(anime: Anime) = repository.addFavorite(anime)

    suspend fun removeFavorite(animeId: Int) = repository.removeFavorite(animeId)
}
