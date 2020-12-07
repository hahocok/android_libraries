package com.android.android_libraries.mvp.model.repo

import com.android.android_libraries.mvp.model.api.ApiHolder.getApi
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.cache.ICache
import com.android.android_libraries.mvp.model.cache.PaperCacheImpl
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.ui.NetworkStatus
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class PaperUsersRepoImpl : IUsersRepo {

    private val networkStatus: INetworkStatus = NetworkStatus()
    private val cache: ICache = PaperCacheImpl()

    override fun getUser(username: String): Single<User> {
        return if (networkStatus.isOnline()) {
            getApi()
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

    override fun getRepositories(user: User): Single<List<Repository>> {
        return if (networkStatus.isOnline()) {
            getApi().getUserRepositories(user.repos_url)
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