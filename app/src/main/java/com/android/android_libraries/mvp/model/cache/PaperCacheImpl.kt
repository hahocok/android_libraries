package com.android.android_libraries.mvp.model.cache

import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import io.paperdb.Paper
import io.reactivex.Single
import java.lang.RuntimeException

class PaperCacheImpl : ICache {
    override fun saveUser(login: String, user: User) {
        Paper.book("users").write(login, user)
    }

    override fun getUser(login: String): Single<User> {
        if (!Paper.book("users").contains(login)) {
            return Single.error(RuntimeException("no such user in cache"))
        }
        return Paper.book("users").read(login)
    }

    override fun saveUserRepository(user: User, repositories: MutableList<Repository>) {
        Paper.book("repos").write(user.login, repositories)
    }

    override fun getUserRepositories(login: String): Single<List<Repository>> {
        if (!Paper.book("repos").contains(login)) {
            return Single.error(RuntimeException("no repos for such user in cache"))
        }
        return Paper.book("repos").read(login)
    }
}