package com.android.android_libraries.mvp.model.repo

import com.android.android_libraries.mvp.model.api.ApiHolder
import com.android.android_libraries.mvp.model.entity.Repository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RepositoriesRepo {
    fun getRepositories(url: String): Single<List<Repository>> {
        return ApiHolder.getApi().getRepositories(url).subscribeOn(Schedulers.io())
    }
}