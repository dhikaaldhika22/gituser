package com.example.gituser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gituser.Userr
import com.example.gituser.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.gituser.data.Result
import com.example.gituser.data.local.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserDetailViewModel(private val repository: FavoriteRepository) : ViewModel() {
    private val _userDetail = MutableStateFlow<Result<Userr>>(Result.Loading)
    val userDetail = _userDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getDetailUser(username: String) {
        _userDetail.value = Result.Loading
        viewModelScope.launch {
            repository.getUserDetail(username).collect {
                _userDetail.value = it
            }
        }
        _isLoading.value = true
    }

    fun isFavorite(id: String): Flow<Boolean> = repository.isFavorite(id)

    fun saveToFavorite(user: UserEntity) {
        viewModelScope.launch {
            repository.saveFavorite(user)
        }
    }

    fun deleteFavorite(users: UserEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(users)
        }
    }
}