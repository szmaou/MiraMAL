package com.szmaou.miramal.domain.usecase

import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.repository.AnimeRepository
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(animeId: Int): Result<Anime> =
        repository.getAnimeDetail(animeId)
}
