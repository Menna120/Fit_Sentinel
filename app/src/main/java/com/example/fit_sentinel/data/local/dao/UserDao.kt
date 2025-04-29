package com.example.fit_sentinel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fit_sentinel.data.local.entity.UserDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userData: UserDataEntity): Long

    @Query("SELECT * FROM user_data LIMIT 1")
    fun getUserDataFlow(): Flow<UserDataEntity?>

    @Query("SELECT * FROM user_data WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): UserDataEntity?

    @Query("SELECT * FROM user_data LIMIT 1")
    suspend fun getUserData(): UserDataEntity?


    @Update
    suspend fun updateUserData(userData: UserDataEntity): Int


    @Delete
    suspend fun deleteUserData(userData: UserDataEntity): Int

    @Query("DELETE FROM user_data")
    suspend fun deleteAllUsers()

    @Query("SELECT COUNT(id) FROM user_data")
    suspend fun getUserCount(): Int
}