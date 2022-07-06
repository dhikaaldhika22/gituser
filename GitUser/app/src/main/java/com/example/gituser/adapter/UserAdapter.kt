package com.example.gituser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gituser.SimpleUser
import com.example.gituser.Util.Companion.setAndVisible
import com.example.gituser.Util.Companion.setImageGlide
import com.example.gituser.databinding.UserListBinding

class UserAdapter(private val listuser: ArrayList<SimpleUser>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

        fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
            this.onItemClickCallback = onItemClickCallback
        }

    class ListViewHolder(var binding: UserListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listuser[position]

        holder.binding.apply {
            tvUsername.text = user.login
            tvType.setAndVisible(user.type)
            imgItemPhoto.setImageGlide(holder.itemView.context, user.avatarUrl)
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
    }

    override fun getItemCount(): Int = listuser.size

    interface OnItemClickCallback {
        fun onItemClicked(user: SimpleUser)
    }

}