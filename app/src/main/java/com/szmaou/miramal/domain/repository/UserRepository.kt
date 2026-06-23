package com.szmaou.miramal.domain.repository

import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.model.UserAnime

interface UserRepository {
    suspend fun getUserAnimeList(status: String? = null, offset: Int = 0): Result<List<UserAnime>>
    suspend fun updateAnimeListStatus(animeId: Int, status: String, score: Int = 0, numWatchedEpisodes: Int = 0): Result<Unit>
    suspend fun deleteAnimeFromList(animeId: Int): Result<Unit>
}
