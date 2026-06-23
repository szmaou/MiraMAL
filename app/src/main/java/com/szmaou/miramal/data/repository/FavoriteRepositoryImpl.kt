package com.szmaou.miramal.data.repository

import com.szmaou.miramal.data.local.dao.FavoriteDao
import com.szmaou.miramal.data.mapper.toAnime
import com.szmaou.miramal.data.mapper.toFavoriteEntity
import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<Anime>> =
        dao.getAllFavorites().map { entities ->
            entities.map { it.toAnime() }
        }

    override fun isFavorite(animeId: Int): Flow<Boolean> = dao.isFavorite(animeId)

    override suspend fun addFavorite(anime: Anime) {
        dao.insert(anime.toFavoriteEntity())
    }

    override suspend fun removeFavorite(animeId: Int) {
        dao.deleteById(animeId)
    }
}
