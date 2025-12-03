package com.example.hellocompose.domain.repository

import com.example.hellocompose.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: Int): User?
}
