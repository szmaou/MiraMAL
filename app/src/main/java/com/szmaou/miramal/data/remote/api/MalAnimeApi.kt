package com.szmaou.miramal.data.remote.api

import com.szmaou.miramal.data.remote.dto.MalAnimeDetailResponse
import com.szmaou.miramal.data.remote.dto.MalAnimeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MalAnimeApi {

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("fields") fields: String = "id,title,main_picture,media_type,num_episodes,mean,rank,popularity"
    ): MalAnimeSearchResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") animeId: Int,
        @Query("fields") fields: String = "id,title,main_picture,alternative_titles,synopsis,mean,rank,popularity,num_episodes,media_type,status,genres,studios,start_season,average_episode_duration,num_listing_users,nsfw,created_at,updated_at"
    ): MalAnimeDetailResponse
}
