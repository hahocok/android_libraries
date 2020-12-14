package com.android.android_libraries.mvp.model.repo

import com.android.android_libraries.mvp.model.api.IDataSource
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.cache.ICache
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UsersRepo(
    private val networkStatus: INetworkStatus,
    private val api: IDataSource,
    private val cache: ICache
) {

    fun getUser(username: String): Single<User> {
        return if (networkStatus.isOnline()) {
            api
                .getUser(username)
                .subscribeOn(Schedulers.io())
                .map { user ->
                cache.saveUser(
                    username,
                    user
                )
                user
            }
        } else {
            cache.getUser(username)
        }

    }

    fun getRepositories(user: User): Single<List<Repository>> {
        return if (networkStatus.isOnline()) {
            api.getUserRepositories(user.repos_url)
                .map { repos ->
                    cache.saveUserRepository(user, repos.toMutableList())
                    repos
                }
                .subscribeOn(Schedulers.io())
        } else {
            cache.getUserRepositories(user.login)
        }
    }

}