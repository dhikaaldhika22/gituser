package com.example.gituser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gituser.SimpleUser
import com.example.gituser.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.gituser.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val repository: FavoriteRepository) : ViewModel() {
    private val _users = MutableStateFlow<Result<ArrayList<SimpleUser>>>(Result.Loading)
    val users = _users.asStateFlow()

    val themeSetting: Flow<Boolean> = repository.getThemeSetting()

    init {
        findUser("\"\"\"")
    }

    fun findUser(query: String) {
        _users.value = Result.Loading
        viewModelScope.launch {
            repository.findUser(query).collect {
                _users.value = it
            }
        }
    }
}