package com.android.android_libraries.mvp.model.cache

import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import io.reactivex.Single

interface ICache {
    fun saveUser(login: String, user: User)
    fun getUser(login: String): Single<User>

    fun saveUserRepository(user: User, repositories: MutableList<Repository>)
    fun getUserRepositories(login: String): Single<List<Repository>>
}