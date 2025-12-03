package com.example.hellocompose.data.repository

import com.example.hellocompose.domain.model.User
import com.example.hellocompose.domain.repository.UserRepository
import kotlinx.coroutines.delay

class UserRepositoryImpl : UserRepository {
    private val users = listOf(
        User(1, "Олена", "olena@example.com"),
        User(2, "Ігор", "ihor@example.com"),
        User(3, "Марія", "maria@example.com"),
        User(4, "Андрій", "andriy@example.com"),
        User(5, "Наталія", "natalia@example.com")
    )

    override suspend fun getUsers(): List<User> {
        delay(1000) // імітація завантаження з мережі
        return users
    }

    override suspend fun getUserById(id: Int): User? {
        delay(500) // імітація завантаження
        return users.find { it.id == id }
    }
}
