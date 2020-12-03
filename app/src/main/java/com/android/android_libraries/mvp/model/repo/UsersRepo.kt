package com.android.android_libraries.mvp.model.repo

import com.android.android_libraries.mvp.model.api.ApiHolder
import com.android.android_libraries.mvp.model.entity.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UsersRepo {
    fun getUser(username: String): Single<User> {
        return ApiHolder.getApi().getUser(username).subscribeOn(Schedulers.io())
    }
}