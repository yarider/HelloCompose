package com.example.hellocompose.domain.usecase

import com.example.hellocompose.domain.model.User
import com.example.hellocompose.domain.repository.UserRepository

class GetUsersUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(): List<User> = repo.getUsers()
}
