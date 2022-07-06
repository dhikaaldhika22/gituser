package com.example.gituser

import com.google.gson.annotations.SerializedName

data class ResponseSearch(
    @field:SerializedName("items")
    val items: ArrayList<SimpleUser>
)

data class Userr(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("company")
    val company: String?,

    @field:SerializedName("location")
    val location: String?,

    @field:SerializedName("public_repos")
    val publicRepos: Int,
)

data class SimpleUser(
    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("type")
    val type: String
)