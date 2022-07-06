package com.example.gituser.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: UserEntity)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getUsers() : Flow<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND favorite = 1)")
    fun favoriteUser(id: String): Flow<Boolean>

    @Update
    suspend fun update(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)
}