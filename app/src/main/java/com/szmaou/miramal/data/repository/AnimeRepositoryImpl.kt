package com.szmaou.miramal.data.repository

import com.szmaou.miramal.data.mapper.toDomain
import com.szmaou.miramal.data.remote.api.MalAnimeApi
import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.repository.AnimeRepository
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: MalAnimeApi
) : AnimeRepository {

    override suspend fun searchAnime(query: String, page: Int): Result<List<Anime>> = runCatching {
        val offset = (page - 1) * 25
        val response = api.searchAnime(query = query, offset = offset)
        response.data.map { it.node.toDomain() }
    }

    override suspend fun getAnimeDetail(animeId: Int): Result<Anime> = runCatching {
        val response = api.getAnimeDetail(animeId)
        response.data.toDomain()
    }
}
