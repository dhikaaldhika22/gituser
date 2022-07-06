package com.example.gituser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gituser.viewmodel.MainViewModel
import com.example.gituser.R
import com.example.gituser.SimpleUser
import com.example.gituser.adapter.UserAdapter
import com.example.gituser.ui.UserDetail.Companion.EXTRA_DATA
import com.example.gituser.databinding.ActivityMainBinding
import com.example.gituser.viewmodel.Factory
import com.example.gituser.data.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels {
        Factory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.themeSetting.collect { state ->
                        if (state) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
                launch {
                    mainViewModel.users.collect { result ->
                        showSearchResult(result)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.searching).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.search_username)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.findUser(query ?: "")
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                Intent(this, UserFavorite::class.java).also {
                    startActivity(it)
                }
            }

            R.id.settings -> {
                Intent(this, SettingsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSearchResult(result: Result<ArrayList<SimpleUser>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                errOcc()
                showLoading(false)
            }
            is Result.Success -> {
                binding.tvResultCount.text = getString(R.string.showing_results, result.data.size)
                val listUserAdapter = UserAdapter(result.data)

                binding.rvUsers.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = listUserAdapter
                    setHasFixedSize(true)
                }

                listUserAdapter.setOnItemClickCallback(object :
                    UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(user: SimpleUser) {
                        toDetailUser(user)
                    }

                })
                showLoading(false)
            }
        }
    }

    private fun errOcc() {
        Toast.makeText(this@MainActivity, "An error occurred", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }

    private fun toDetailUser(user: SimpleUser) {
        Intent(this@MainActivity, UserDetail::class.java).apply {
            putExtra(EXTRA_DATA, user.login)
        }.also {
            startActivity(it)
        }
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}