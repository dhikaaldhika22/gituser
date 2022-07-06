package com.example.gituser.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gituser.viewmodel.FollowersViewModel
import com.example.gituser.adapter.SectionPageAdapter.Companion.ARGS_USERNAME
import com.example.gituser.SimpleUser
import com.example.gituser.adapter.UserAdapter
import com.example.gituser.databinding.FragmentFollowersBinding
import com.example.gituser.viewmodel.Factory
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import com.example.gituser.data.Result

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private val followersViewModel by viewModels<FollowersViewModel> {
        Factory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowersBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARGS_USERNAME) ?: ""
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                followersViewModel.followers.collect { result ->
                    onFollowersResult(result)
                }
            }
            launch {
                followersViewModel.isLoading.collect { loaded ->
                    if (!loaded) followersViewModel.getFollowers(username)
                }
            }
        }
    }

    private fun onFollowersResult(result: Result<ArrayList<SimpleUser>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                showLoading(false)
            }
            is Result.Success -> {
                showFollowers(result.data)
                showLoading(false)
            }
        }
    }

    private fun showFollowers(users: ArrayList<SimpleUser>) {
        if (users.size > 0) {
            val linearLayoutManager = LinearLayoutManager(activity)
            val listAdapter = UserAdapter(users)

            binding.rvUsers.apply {
                layoutManager = linearLayoutManager
                adapter = listAdapter
                setHasFixedSize(true)
            }

            listAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: SimpleUser) {
                    toDetailUser(user)
                }
            })
        } else {
            binding.tvStatus.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }

    private fun toDetailUser(user: SimpleUser) {
        Intent(activity, UserDetail::class.java).apply {
            putExtra(UserDetail.EXTRA_DATA, user.login)
        }.also {
            startActivity(it)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}