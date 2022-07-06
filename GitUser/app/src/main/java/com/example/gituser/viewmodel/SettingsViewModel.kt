package com.example.gituser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gituser.data.repository.FavoriteRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class SettingsViewModel(private val userRepository: FavoriteRepository) : ViewModel() {
    val getTheme: Flow<Boolean> = userRepository.getThemeSetting()

    fun saveTheme(darkMode: Boolean) {
        viewModelScope.launch {
            userRepository.saveThemeSetting(darkMode)
        }
    }
}