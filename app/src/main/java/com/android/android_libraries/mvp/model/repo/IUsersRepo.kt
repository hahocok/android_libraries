package com.android.android_libraries.mvp.model.repo

import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import io.reactivex.Single

interface IUsersRepo {
    fun getUser(username: String): Single<User>
    fun getRepositories(user: User): Single<List<Repository>>
}