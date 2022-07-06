package com.example.gituser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.gituser.R
import com.example.gituser.adapter.SectionPageAdapter
import com.example.gituser.viewmodel.UserDetailViewModel
import com.example.gituser.Userr
import com.example.gituser.Util.Companion.setAndVisible
import com.example.gituser.Util.Companion.setImageGlide
import com.example.gituser.data.local.UserEntity
import com.example.gituser.databinding.UserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import com.example.gituser.data.Result
import com.example.gituser.viewmodel.Factory

class UserDetail : AppCompatActivity(), View.OnClickListener {
    private var _binding: UserDetailBinding? = null
    private val binding get() = _binding!!

    private var name: String? = null
    private var username: String? = null
    private var profileUrl: String? = null
    private var userDetail: UserEntity? = null
    private var isFavorite: Boolean? = null

    private val detailViewModel: UserDetailViewModel by viewModels {
        Factory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = UserDetailBinding.inflate(layoutInflater)
        username = intent.extras?.get(EXTRA_DATA) as String
        name = intent.extras?.get(EXTRA_DATA) as String

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(binding.root)
        setViewPager()
        setToolbar()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    detailViewModel.userDetail.collect { result ->
                        onUserDetail(result)
                    }
                }
                launch {
                    detailViewModel.isFavorite(username ?: "").collect { state ->
                        isFavorite(state)
                        isFavorite = state
                    }
                }
                launch {
                    detailViewModel.isLoading.collect { loaded ->
                        if (!loaded) detailViewModel.getDetailUser(username ?: "")
                    }
                }
            }
        }
        binding.fabFavorite.setOnClickListener(this)
    }

    private fun onUserDetail(result: Result<Userr>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                errorOcc()
                showLoading(false)
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
            is Result.Success -> {
                result.data.let { user ->
                    parseUserDetail(user)

                    val userEntity = UserEntity(user.login, user.avatarUrl, true)
                    userDetail = userEntity
                    profileUrl = user.htmlUrl
                }
                showLoading(false)
            }
        }
    }

    private fun isFavorite(favorite: Boolean) {
        if (favorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_28)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_favorite -> {
                if (isFavorite == false) {
                    userDetail?.let {
                        detailViewModel.saveToFavorite(it)
                    }
                    isFavorite(true)
                    Toast.makeText(this, "User added to favorite", Toast.LENGTH_SHORT).show()
                } else {
                    userDetail?.let {
                        detailViewModel.deleteFavorite(it)
                    }
                    isFavorite(false)
                    Toast.makeText(this, "User removed to favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_menu -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hello! $username is available in GitHub. You can check the profile by this link : https://github.com/$username")
                    type = "text/plain"
                }
                val openProfile = Intent.createChooser(sendIntent, null)
                startActivity(openProfile)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbarDetail)
        binding.collapsingToolbar.isTitleEnabled = false
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.profile)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun setViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        viewPager.adapter = SectionPageAdapter(this, username!!)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun errorOcc() {
        binding.apply {
            userDetailContainer.visibility = View.INVISIBLE
            tabs.visibility = View.INVISIBLE
            viewPager.visibility = View.INVISIBLE
        }
        Toast.makeText(this@UserDetail, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                pbLoading.visibility = View.VISIBLE
                appBarLayout.visibility = View.INVISIBLE
                viewPager.visibility = View.INVISIBLE
                fabFavorite.visibility = View.GONE
            }
        } else {
            binding.apply {
                pbLoading.visibility = View.GONE
                appBarLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                fabFavorite.visibility = View.VISIBLE
            }
        }
    }

    private fun parseUserDetail(user: Userr) {
        binding.apply {
            tvUsernameDetail.text = user.login
            tvRepositoryDetail.text = user.publicRepos.toString()
            tvFollowersDetail.text = user.followers.toString()
            tvFollowingDetail.text = user.following.toString()
            tvNameDetail.setAndVisible(user.name)
            tvCompanyDetail.setAndVisible(user.company)
            tvLocationDetail.setAndVisible(user.location)
            avatar.setImageGlide(this@UserDetail, user.avatarUrl)
        }
    }

    override fun onDestroy() {
        _binding = null
        name = null
        username = null
        profileUrl = null
        isFavorite = null

        super.onDestroy()
    }

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
        private val TAB_TITLES = intArrayOf(R.string.followers, R.string.following)
    }
}