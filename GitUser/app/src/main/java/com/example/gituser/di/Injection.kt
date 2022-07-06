package com.example.gituser.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.gituser.api.ApiConfig
import com.example.gituser.data.repository.FavoriteRepository
import com.example.gituser.data.local.UserDatabase
import androidx.datastore.preferences.core.Preferences
import com.example.gituser.data.AppPreferences

object Injection {
    fun provideRepo(context: Context, dataStore: DataStore<Preferences>) : FavoriteRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getDatabase(context)
        val userDao = database.userDao()
        val preferences = AppPreferences.getInstance(dataStore)

        return FavoriteRepository.getInstance(apiService, userDao, preferences)
    }
}