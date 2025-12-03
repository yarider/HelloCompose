package com.example.hellocompose.domain.usecase

import com.example.hellocompose.domain.model.User
import com.example.hellocompose.domain.repository.UserRepository

class GetUserByIdUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(id: Int): User? = repo.getUserById(id)
}
