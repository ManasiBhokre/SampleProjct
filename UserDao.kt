package com.example.using_room.Room
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("Select * FROM Users")
    fun getAllUsers(): List<User>

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}