package com.szmaou.miramal.domain.model

data class Anime(
    val malId: Int,
    val title: String,
    val titleEnglish: String?,
    val imageUrl: String,
    val synopsis: String?,
    val type: String?,
    val episodes: Int?,
    val score: Double?,
    val rank: Int?,
    val popularity: Int?,
    val status: String?,
    val year: Int?,
    val season: String?,
    val genres: List<String>,
    val studios: List<String>
)
