package com.bleo.simplegithubserachapplication.main.network

import com.google.gson.JsonElement
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface GithubAPI {

    @GET("search/repositories")
    fun searchRepos(
        @Query("q") search: String
    ): Single<JsonElement>

    @GET("user/starred/{owner}/{repo}")
    fun checkStar(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Completable
}