package com.example.gituser.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>){
    private val theme = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[theme] ?: false
        }
    }

    suspend fun saveThemeSetting(darkModeState: Boolean) {
        dataStore.edit { preferences ->
            preferences[theme] = darkModeState
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AppPreferences {
            return INSTANCE ?: synchronized(this) {
               val instance = AppPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}