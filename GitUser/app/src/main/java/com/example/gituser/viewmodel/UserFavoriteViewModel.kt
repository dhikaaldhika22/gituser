package com.example.gituser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gituser.data.repository.FavoriteRepository
import com.example.gituser.data.local.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserFavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {
    private val _favorite = MutableStateFlow(listOf<UserEntity>())
    val favorite = _favorite.asStateFlow()

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            repository.getFavorite().collect {
                _favorite.value = it
            }
        }
    }
}