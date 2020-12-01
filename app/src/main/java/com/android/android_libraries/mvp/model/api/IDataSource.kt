package com.android.android_libraries.mvp.model.api

import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface IDataSource {
    @GET("/users/{user}")
    fun getUser(@Path("user") username: String?): Single<User?>?

    @GET
    fun getRepositories(@Url url: String?): Single<List<Repository?>?>?
}