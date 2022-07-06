package com.example.gituser.data.repository

import android.util.Log
import com.example.gituser.SimpleUser
import com.example.gituser.Userr
import com.example.gituser.Util
import com.example.gituser.api.ApiService
import com.example.gituser.data.AppPreferences
import com.example.gituser.data.Result
import com.example.gituser.data.local.UserDao
import com.example.gituser.data.local.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class FavoriteRepository private constructor(private val apiService: ApiService, private val userDao: UserDao, private val appPreferences: AppPreferences) {
    companion object {
        private const val API_TOKEN = "Bearer ${Util.TOKEN}"
        private val TAG = FavoriteRepository::class.java.simpleName
        private var INSTANCE: FavoriteRepository? = null

        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            preferences: AppPreferences
        ): FavoriteRepository {
            return INSTANCE ?: synchronized(this) {
                FavoriteRepository(apiService, userDao, preferences).also {
                    INSTANCE = it
                }
            }
        }
    }

    fun findUser(query: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
        emit(Result.Loading)

        try {
            val user = apiService.searchUsername(token = API_TOKEN, query).items
            emit(Result.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowers(id: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
        emit(Result.Loading)

        try {
            val user = apiService.getFollowers(token = API_TOKEN, id)
            emit(Result.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowing(id: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
        emit(Result.Loading)

        try {
            val user = apiService.getFollowing(token = API_TOKEN, id)
            emit(Result.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserDetail(id: String): Flow<Result<Userr>> = flow {
        emit(Result.Loading)

        try {
            val user = apiService.getDetailUser(token = API_TOKEN, id)
            emit(Result.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun isFavorite(id: String): Flow<Boolean> {
       return userDao.favoriteUser(id)
    }

    fun getFavorite(): Flow<List<UserEntity>> {
        return userDao.getUsers()
    }

    suspend fun saveFavorite(user: UserEntity) {
        userDao.insert(user)
    }

    suspend fun deleteFavorite(user: UserEntity) {
        userDao.delete(user)
    }

    fun getThemeSetting(): Flow<Boolean> = appPreferences.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        appPreferences.saveThemeSetting(isDarkModeActive)
    }
}