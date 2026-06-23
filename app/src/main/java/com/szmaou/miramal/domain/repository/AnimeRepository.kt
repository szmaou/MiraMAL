package com.szmaou.miramal.domain.repository

import com.szmaou.miramal.domain.model.Anime

interface AnimeRepository {
    suspend fun searchAnime(query: String, page: Int): Result<List<Anime>>
    suspend fun getAnimeDetail(animeId: Int): Result<Anime>
}
