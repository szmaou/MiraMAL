package com.szmaou.miramal.domain.repository

import com.szmaou.miramal.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<Anime>>
    fun isFavorite(animeId: Int): Flow<Boolean>
    suspend fun addFavorite(anime: Anime)
    suspend fun removeFavorite(animeId: Int)
}
