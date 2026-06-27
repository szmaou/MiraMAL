package com.szmaou.miramal.data.remote.auth

import android.net.Uri
import android.util.Log
import java.security.SecureRandom
import java.util.Base64
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MalAuthManager @Inject constructor(
    private val tokenStore: AuthTokenStore,
    private val authApi: MalAuthApi,
    @Named("MAL_CLIENT_ID") private val clientId: String,
    @Named("MAL_REDIRECT_URI") private val redirectUri: String
) {
    private companion object {
        private const val AUTH_URL = "https://myanimelist.net/v1/oauth2/authorize"
    }

    private var currentCodeVerifier: String? = null

    val isLoggedIn: Boolean
        get() = tokenStore.getToken() != null

    fun buildAuthUrl(): String {
        val verifier = generateCodeVerifier()
        currentCodeVerifier = verifier
        Log.e("MiraMAL", "buildAuthUrl: verifier(${verifier.length})=${verifier.take(12)}...")

        return Uri.parse(AUTH_URL).buildUpon()
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("state", generateState())
            .appendQueryParameter("code_challenge", verifier)
            .build()
            .toString()
    }

    suspend fun exchangeCode(code: String) {
        val verifier = currentCodeVerifier ?: throw Exception("No PKCE verifier found")
        currentCodeVerifier = null
        Log.e("MiraMAL", "exchangeCode: code=${code.take(8)}..., verifier=${verifier.take(8)}...")

        val tokenResponse = authApi.exchangeCode(
            clientId = clientId,
            code = code,
            codeVerifier = verifier
        )

        tokenStore.saveToken(
            AuthToken(
                accessToken = tokenResponse.accessToken,
                refreshToken = tokenResponse.refreshToken,
                expiresIn = tokenResponse.expiresIn
            )
        )
    }

    suspend fun refreshToken(): String? {
        val currentToken = tokenStore.getToken() ?: return null
        return try {
            val tokenResponse = authApi.refreshToken(clientId, currentToken.refreshToken)
            val newToken = AuthToken(
                accessToken = tokenResponse.accessToken,
                refreshToken = tokenResponse.refreshToken,
                expiresIn = tokenResponse.expiresIn
            )
            tokenStore.saveToken(newToken)
            newToken.accessToken
        } catch (_: Exception) {
            tokenStore.clear()
            null
        }
    }

    fun getAccessToken(): String? = tokenStore.getToken()?.accessToken

    fun logout() = tokenStore.clear()

    private fun generateCodeVerifier(): String {
        val bytes = ByteArray(64)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun generateState(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}
