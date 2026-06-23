package com.szmaou.miramal.data.remote.api

import com.szmaou.miramal.data.remote.dto.MalUpdatedListStatus
import com.szmaou.miramal.data.remote.dto.MalUpdateListStatusRequest
import com.szmaou.miramal.data.remote.dto.MalUserAnimeListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MalUserApi {

    @GET("users/@me/animelist")
    suspend fun getUserAnimeList(
        @Query("status") status: String? = null,
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("fields") fields: String = "id,title,main_picture,media_type,num_episodes,mean,rank"
    ): MalUserAnimeListResponse

    @PATCH("anime/{id}/my_list_status")
    suspend fun updateAnimeListStatus(
        @Path("id") animeId: Int,
        @Body request: MalUpdateListStatusRequest
    ): MalUpdatedListStatus

    @DELETE("anime/{id}/my_list_status")
    suspend fun deleteAnimeFromList(@Path("id") animeId: Int)
}
