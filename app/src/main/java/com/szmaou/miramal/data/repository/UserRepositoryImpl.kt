package com.szmaou.miramal.data.repository

import com.szmaou.miramal.data.mapper.toDomain
import com.szmaou.miramal.data.remote.api.MalUserApi
import com.szmaou.miramal.data.remote.dto.MalUpdateListStatusRequest
import com.szmaou.miramal.domain.model.MyListStatus
import com.szmaou.miramal.domain.model.UserAnime
import com.szmaou.miramal.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: MalUserApi
) : UserRepository {

    override suspend fun getUserAnimeList(status: String?, offset: Int): Result<List<UserAnime>> = runCatching {
        val response = api.getUserAnimeList(status = status, offset = offset)
        response.data.map { node ->
            UserAnime(
                anime = node.node.toDomain(),
                listStatus = node.listStatus?.let {
                    MyListStatus(
                        status = it.status ?: "",
                        score = it.score ?: 0,
                        numEpisodesWatched = it.numEpisodesWatched ?: 0,
                        isRewatching = it.isRewatching ?: false
                    )
                }
            )
        }
    }

    override suspend fun updateAnimeListStatus(
        animeId: Int,
        status: String,
        score: Int,
        numWatchedEpisodes: Int
    ): Result<Unit> = runCatching {
        api.updateAnimeListStatus(
            animeId = animeId,
            request = MalUpdateListStatusRequest(
                status = status,
                score = score,
                numWatchedEpisodes = numWatchedEpisodes
            )
        )
        Unit
    }

    override suspend fun deleteAnimeFromList(animeId: Int): Result<Unit> = runCatching {
        api.deleteAnimeFromList(animeId)
    }
}
