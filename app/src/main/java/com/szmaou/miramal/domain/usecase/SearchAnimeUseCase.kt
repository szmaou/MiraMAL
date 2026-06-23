package com.szmaou.miramal.domain.usecase

import com.szmaou.miramal.domain.model.Anime
import com.szmaou.miramal.domain.repository.AnimeRepository
import javax.inject.Inject

class SearchAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(query: String, page: Int = 1): Result<List<Anime>> =
        repository.searchAnime(query, page)
}
