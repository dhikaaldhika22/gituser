package com.example.gituser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gituser.R
import com.example.gituser.SimpleUser
import com.example.gituser.adapter.UserAdapter
import com.example.gituser.data.local.UserEntity
import com.example.gituser.databinding.ActivityUserFavoriteBinding
import com.example.gituser.viewmodel.Factory
import com.example.gituser.viewmodel.UserFavoriteViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class UserFavorite : AppCompatActivity() {

    private lateinit var bindingFavorite: ActivityUserFavoriteBinding
    private val favoriteViewModel: UserFavoriteViewModel by viewModels {
        Factory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingFavorite = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(bindingFavorite.root)

        setToolbar(getString(R.string.favorite))

        lifecycleScope.launchWhenStarted {
            launch {
                favoriteViewModel.favorite.collect {
                    if (it.isNotEmpty()) showFavorite(it)
                    else showInfo()
                }
            }
        }
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(bindingFavorite.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun showInfo() {
        bindingFavorite.noData.visibility = View.VISIBLE
        bindingFavorite.rvFav.visibility = View.GONE
    }

    private fun showFavorite(users: List<UserEntity>) {

        val list = ArrayList<SimpleUser>()

        users.forEach { user ->
            val data = SimpleUser(
                user.avatarUrl,
                user.id,
                user.id
            )
            list.add(data)
        }

        val userAdapter = UserAdapter(list)

        bindingFavorite.rvFav.apply {
            layoutManager = LinearLayoutManager(this@UserFavorite)
            adapter = userAdapter
            visibility = View.VISIBLE
            setHasFixedSize(true)
        }

        bindingFavorite.noData.visibility = View.GONE

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: SimpleUser) {
                goToDetailUser(user)
            }
        })
    }

    private fun goToDetailUser(user: SimpleUser) {
        Intent(this@UserFavorite, UserDetail::class.java).apply {
            putExtra(UserDetail.EXTRA_DATA, user.login)
        }.also {
            startActivity(it)
        }
    }
}