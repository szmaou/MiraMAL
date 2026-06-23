package com.szmaou.miramal.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalUserAnimeListResponse(
    val data: List<MalUserAnimeNode>,
    val paging: MalPaging? = null
)

@Serializable
data class MalUserAnimeNode(
    val node: MalAnimeDto,
    @SerialName("list_status") val listStatus: MalListStatus
)

@Serializable
data class MalListStatus(
    val status: String? = null,
    val score: Int? = null,
    @SerialName("num_episodes_watched") val numEpisodesWatched: Int? = null,
    @SerialName("is_rewatching") val isRewatching: Boolean? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class MalUpdateListStatusRequest(
    val status: String? = null,
    val score: Int? = null,
    @SerialName("num_watched_episodes") val numWatchedEpisodes: Int? = null
)

@Serializable
data class MalUpdatedListStatus(
    val status: String? = null,
    val score: Int? = null,
    @SerialName("num_watched_episodes") val numWatchedEpisodes: Int? = null,
    @SerialName("is_rewatching") val isRewatching: Boolean? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)
