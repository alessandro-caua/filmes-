package com.outracoisa.avaliacao3appn2.data.repository

import com.outracoisa.avaliacao3appn2.data.local.dao.UserDao
import com.outracoisa.avaliacao3appn2.data.local.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun register(username: String, password: String, name: String): Result<User> {
        return try {
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                Result.failure(Exception("Usuário já existe"))
            } else {
                val user = User(username = username, password = password, name = name)
                val id = userDao.insert(user)
                Result.success(user.copy(id = id.toInt()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, password: String): Result<User> {
        return try {
            val user = userDao.login(username, password)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Usuário ou senha inválidos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
}
