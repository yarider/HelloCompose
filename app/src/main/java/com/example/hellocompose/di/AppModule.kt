package com.example.hellocompose.di

import com.example.hellocompose.data.repository.UserRepositoryImpl
import com.example.hellocompose.domain.repository.UserRepository
import com.example.hellocompose.domain.usecase.GetUserByIdUseCase
import com.example.hellocompose.domain.usecase.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepositoryImpl()

    @Provides
    fun provideGetUsersUseCase(repo: UserRepository) = GetUsersUseCase(repo)

    @Provides
    fun provideGetUserByIdUseCase(repo: UserRepository) = GetUserByIdUseCase(repo)
}
