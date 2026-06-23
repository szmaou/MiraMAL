package com.szmaou.miramal.data.remote.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authManager: MalAuthManager
) : Authenticator {

    override fun authenticate(route: Route?, response: okhttp3.Response): okhttp3.Request? {
        if (response.request.header("Authorization") == null) return null

        return runBlocking {
            val newToken = authManager.refreshToken()
            newToken?.let { token ->
                response.request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            }
        }
    }
}
