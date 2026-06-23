package com.szmaou.miramal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val imageUrl: String,
    val score: Double?,
    val type: String?,
    val savedAt: Long = System.currentTimeMillis()
)
