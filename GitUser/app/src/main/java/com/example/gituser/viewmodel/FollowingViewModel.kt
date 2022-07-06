package com.example.gituser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gituser.SimpleUser
import com.example.gituser.data.repository.FavoriteRepository
import com.example.gituser.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FollowingViewModel(private val repository: FavoriteRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _following = MutableStateFlow<Result<ArrayList<SimpleUser>>>(Result.Loading)
    val following = _following.asStateFlow()

    fun getFollowing(username: String) {
        _following.value = Result.Loading

        viewModelScope.launch {
            repository.getUserFollowing(username).collect {
                _following.value = it
            }
        }
        _isLoading.value = true
    }
}