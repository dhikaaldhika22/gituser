package com.example.gituser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gituser.SimpleUser
import com.example.gituser.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.gituser.data.Result

class FollowersViewModel(private val repository: FavoriteRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _followers = MutableStateFlow<Result<ArrayList<SimpleUser>>>(Result.Loading)
    val followers = _followers.asStateFlow()

    fun getFollowers(username: String) {
        _followers.value = Result.Loading

        viewModelScope.launch {
            repository.getUserFollowers(username).collect {
                _followers.value = it
            }
        }
        _isLoading.value = true
    }
}