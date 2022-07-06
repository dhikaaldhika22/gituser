package com.example.gituser.api

import com.example.gituser.ResponseSearch
import com.example.gituser.SimpleUser
import com.example.gituser.Userr
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun searchUsername(
        @Header("Authorization") token: String,
        @Query("q") q: String
    ) : ResponseSearch

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Userr

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): ArrayList<SimpleUser>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): ArrayList<SimpleUser>
}