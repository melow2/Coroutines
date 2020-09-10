package com.khs.coroutines

import kotlinx.coroutines.delay

class UserRepository {
    suspend fun getUsers() : List<User>{
        delay(5000)
        val users:List<User> = listOf(
            User(1,"Sam1"),
            User(2,"Sam2"),
            User(3,"Sam3"),
            User(4,"Sam4")
        )
        return users
    }
}