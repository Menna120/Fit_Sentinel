package com.example.fit_sentinel.data.repository

import com.example.fit_sentinel.data.local.dao.UserDao
import com.example.fit_sentinel.data.local.entity.UserDataEntity
import com.example.fit_sentinel.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserDataRepository {

    override fun getUserData(): Flow<UserDataEntity?> {
        return userDao.getUserDataFlow()
    }

    override suspend fun getUserById(userId: Int): UserDataEntity? {
        return userDao.getUserById(userId)
    }

    override suspend fun saveUserData(userData: UserDataEntity) {
        userDao.insertUserData(userData)
    }

    override suspend fun updateUserData(userData: UserDataEntity) {
        userDao.updateUserData(userData)
    }

    override suspend fun deleteUserData(userData: UserDataEntity) {
        userDao.deleteUserData(userData)
    }

    override suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    override suspend fun getUserCount(): Int {
        return userDao.getUserCount()
    }
}