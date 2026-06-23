package com.szmaou.miramal.di

import com.szmaou.miramal.data.repository.AnimeRepositoryImpl
import com.szmaou.miramal.data.repository.FavoriteRepositoryImpl
import com.szmaou.miramal.data.repository.UserRepositoryImpl
import com.szmaou.miramal.domain.repository.AnimeRepository
import com.szmaou.miramal.domain.repository.FavoriteRepository
import com.szmaou.miramal.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnimeRepository(impl: AnimeRepositoryImpl): AnimeRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
