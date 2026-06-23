package com.szmaou.miramal.di

import com.szmaou.miramal.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    @Named("MAL_CLIENT_ID")
    fun provideClientId(): String = BuildConfig.MAL_CLIENT_ID

    @Provides
    @Singleton
    @Named("MAL_REDIRECT_URI")
    fun provideRedirectUri(): String = "miramal://auth"
}
