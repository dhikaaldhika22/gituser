package com.example.gituser.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gituser.data.repository.FavoriteRepository
import com.example.gituser.di.Injection
import com.example.gituser.ui.dataStore

class Factory(private val userRepository: FavoriteRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(mClass: Class<T>): T {
        return when {
            mClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            mClass.isAssignableFrom(UserDetailViewModel::class.java) -> {
                UserDetailViewModel(userRepository) as T
            }
            mClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                FollowersViewModel(userRepository) as T
            }
            mClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                FollowingViewModel(userRepository) as T
            }
            mClass.isAssignableFrom(UserFavoriteViewModel::class.java) -> {
                UserFavoriteViewModel(userRepository) as T
            }
            mClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${mClass.name}")
        }

    }

    companion object {
        private var INSTANCE: Factory? = null

        fun getInstance(context: Context): Factory {
            return INSTANCE ?: synchronized(this) {
                Factory(Injection.provideRepo(context, context.dataStore)).also {
                    INSTANCE = it
                }
            }
        }
    }
}