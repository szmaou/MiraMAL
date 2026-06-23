package com.szmaou.miramal.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalAnimeSearchResponse(
    val data: List<MalAnimeNode>,
    val paging: MalPaging? = null
)

@Serializable
data class MalAnimeDetailResponse(
    val data: MalAnimeFullDto
)

@Serializable
data class MalAnimeFullDto(
    val id: Int,
    val title: String,
    @SerialName("main_picture") val mainPicture: MalPicture? = null,
    @SerialName("alternative_titles") val alternativeTitles: MalAlternativeTitles? = null,
    val synopsis: String? = null,
    val mean: Double? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    @SerialName("num_episodes") val numEpisodes: Int? = null,
    @SerialName("media_type") val mediaType: String? = null,
    val status: String? = null,
    val genres: List<MalGenre>? = null,
    val studios: List<MalStudio>? = null,
    @SerialName("start_season") val startSeason: MalSeason? = null,
    @SerialName("average_episode_duration") val averageEpisodeDuration: Int? = null,
    @SerialName("num_listing_users") val numListingUsers: Int? = null,
    @SerialName("nsfw") val nsfw: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class MalAnimeNode(
    val node: MalAnimeDto
)

@Serializable
data class MalAnimeDto(
    val id: Int,
    val title: String,
    @SerialName("main_picture") val mainPicture: MalPicture? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("num_episodes") val numEpisodes: Int? = null,
    val mean: Double? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    val genres: List<MalGenre>? = null,
    val synopsis: String? = null,
    val status: String? = null,
    val studios: List<MalStudio>? = null,
    @SerialName("start_season") val startSeason: MalSeason? = null
)

@Serializable
data class MalPicture(
    val medium: String? = null,
    val large: String? = null
)

@Serializable
data class MalAlternativeTitles(
    val en: String? = null,
    val ja: String? = null
)

@Serializable
data class MalGenre(
    val id: Int,
    val name: String
)

@Serializable
data class MalStudio(
    val id: Int,
    val name: String
)

@Serializable
data class MalSeason(
    val year: Int? = null,
    val season: String? = null
)

@Serializable
data class MalPaging(
    val next: String? = null
)
