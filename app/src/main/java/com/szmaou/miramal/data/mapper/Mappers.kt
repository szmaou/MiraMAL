package com.szmaou.miramal.data.mapper

import com.szmaou.miramal.data.local.entity.FavoriteEntity
import com.szmaou.miramal.data.remote.dto.MalAnimeDto
import com.szmaou.miramal.data.remote.dto.MalAnimeFullDto
import com.szmaou.miramal.domain.model.Anime

fun MalAnimeDto.toDomain(): Anime = Anime(
    malId = id,
    title = title,
    titleEnglish = null,
    imageUrl = mainPicture?.large ?: mainPicture?.medium ?: "",
    synopsis = synopsis,
    type = mediaType,
    episodes = numEpisodes,
    score = mean,
    rank = rank,
    popularity = popularity,
    status = status,
    year = startSeason?.year,
    season = startSeason?.season,
    genres = genres?.map { it.name } ?: emptyList(),
    studios = studios?.map { it.name } ?: emptyList()
)

fun MalAnimeFullDto.toDomain(): Anime = Anime(
    malId = id,
    title = title,
    titleEnglish = alternativeTitles?.en,
    imageUrl = mainPicture?.large ?: mainPicture?.medium ?: "",
    synopsis = synopsis,
    type = mediaType,
    episodes = numEpisodes,
    score = mean,
    rank = rank,
    popularity = popularity,
    status = status,
    year = startSeason?.year,
    season = startSeason?.season,
    genres = genres?.map { it.name } ?: emptyList(),
    studios = studios?.map { it.name } ?: emptyList()
)

fun Anime.toFavoriteEntity(): FavoriteEntity = FavoriteEntity(
    malId = malId,
    title = title,
    imageUrl = imageUrl,
    score = score,
    type = type
)

fun FavoriteEntity.toAnime(): Anime = Anime(
    malId = malId,
    title = title,
    titleEnglish = null,
    imageUrl = imageUrl,
    synopsis = null,
    type = type,
    episodes = null,
    score = score,
    rank = null,
    popularity = null,
    status = null,
    year = null,
    season = null,
    genres = emptyList(),
    studios = emptyList()
)
