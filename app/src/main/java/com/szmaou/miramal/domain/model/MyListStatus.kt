package com.szmaou.miramal.domain.model

data class MyListStatus(
    val status: String,
    val score: Int,
    val numEpisodesWatched: Int,
    val isRewatching: Boolean
)

data class UserAnime(
    val anime: Anime,
    val listStatus: MyListStatus?
)
