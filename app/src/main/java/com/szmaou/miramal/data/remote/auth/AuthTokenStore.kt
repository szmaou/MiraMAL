package com.szmaou.miramal.data.remote.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val obtainedAt: Long = System.currentTimeMillis()
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() >= obtainedAt + (expiresIn * 1000) - 300_000
}

@Singleton
class AuthTokenStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "miramal_auth",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private var cachedToken: AuthToken? = null

    fun saveToken(token: AuthToken) {
        cachedToken = token
        prefs.edit()
            .putString(KEY_ACCESS, token.accessToken)
            .putString(KEY_REFRESH, token.refreshToken)
            .putLong(KEY_EXPIRES, token.expiresIn)
            .putLong(KEY_OBTAINED, token.obtainedAt)
            .apply()
    }

    fun getToken(): AuthToken? {
        cachedToken?.let { if (!it.isExpired) return it }

        val access = prefs.getString(KEY_ACCESS, null) ?: return null
        val refresh = prefs.getString(KEY_REFRESH, null) ?: return null
        val expires = prefs.getLong(KEY_EXPIRES, 0)
        val obtained = prefs.getLong(KEY_OBTAINED, 0)

        val token = AuthToken(
            accessToken = access,
            refreshToken = refresh,
            expiresIn = expires,
            obtainedAt = obtained
        )
        cachedToken = token
        return token
    }

    fun clear() {
        cachedToken = null
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_EXPIRES = "expires_in"
        private const val KEY_OBTAINED = "obtained_at"
    }
}
