package com.example.apigithubuserapp.api

import com.example.apigithubuserapp.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun getListUsers(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getListFollowers(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getListFollowing(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>


}