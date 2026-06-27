package com.szmaou.miramal.data.remote.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val TOKEN_URL = "https://myanimelist.net/v1/oauth2/token"

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("expires_in") val expiresIn: Long
)

@Singleton
class MalAuthApi @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun exchangeCode(
        clientId: String,
        code: String,
        codeVerifier: String
    ): TokenResponse {
        val body = FormBody.Builder()
            .add("client_id", clientId)
            .add("code", code)
            .add("code_verifier", codeVerifier)
            .add("grant_type", "authorization_code")
            .build()

        return execute(body)
    }

    suspend fun refreshToken(clientId: String, refreshToken: String): TokenResponse {
        val body = FormBody.Builder()
            .add("client_id", clientId)
            .add("refresh_token", refreshToken)
            .add("grant_type", "refresh_token")
            .build()

        return execute(body)
    }

    private suspend fun execute(body: FormBody): TokenResponse {
        val request = Request.Builder()
            .url(TOKEN_URL)
            .post(body)
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
        val json = response.body?.string() ?: throw Exception("Empty response")

        if (!response.isSuccessful) {
            throw Exception("Token request failed (${response.code}): $json")
        }

        return kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        }.decodeFromString<TokenResponse>(json)
    }
}
