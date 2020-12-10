package com.android.android_libraries.mvp.model.cache

import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.mvp.model.entity.room.RoomRepository
import com.android.android_libraries.mvp.model.entity.room.RoomUser
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers


class RoomCacheImpl : ICache {
    override fun saveUser(login: String, user: User) {
//        val roomUser = Database.getInstance().getUserDao().findByLogin(login)
//
//        roomUser?.run {
//            avatarUrl = user.avatar_url
//            reposUrl = user.repos_url
//            name = user.name
//        }
//
//        roomUser?.let { Database.getInstance().getUserDao().insert(it) }
    }

    override fun getUser(login: String): Single<User> {
        return Single.create { emitter: SingleEmitter<Any?> ->
//            val roomUser = Database.getInstance().getUserDao().findByLogin(login)
//            if (roomUser == null) {
//                emitter.onError(RuntimeException("No such user in cache"))
//            } else {
                emitter.onSuccess(
//                    User(
//                        roomUser.login,
//                        roomUser.avatarUrl,
//                        roomUser.reposUrl,
//                        roomUser.name
//                    )
                )
//            }
        }.subscribeOn(Schedulers.io()).cast(User::class.java)
    }

    override fun saveUserRepository(user: User, repositories: MutableList<Repository>) {
//        var roomUser = Database.getInstance().getUserDao().findByLogin(user.login)
//        if (roomUser == null) {
//            roomUser = RoomUser(user.login, user.avatar_url, user.repos_url)
//            Database.getInstance()
//                .getUserDao()
//                .insert(roomUser)
//        }
//
//        if (repositories.isNotEmpty()) {
//            val roomRepositories: MutableList<RoomRepository> = ArrayList()
//            for (repository in repositories) {
//                val roomRepository =
//                    RoomRepository(repository.id, repository.name, user.login)
//                roomRepositories.add(roomRepository)
//            }
//            Database.getInstance()
//                .getRepositoryDao()
//                .insert(roomRepositories)
//        }
    }

    override fun getUserRepositories(login: String): Single<List<Repository>> {
        return Single.create { emitter: SingleEmitter<Any?> ->
//            val roomUser = Database.getInstance().getUserDao().findByLogin(login)
//            if (roomUser == null) {
//                emitter.onError(RuntimeException("No such user in cache"))
//            } else {
//                val roomRepositories: List<RoomRepository>  = Database.getInstance().getRepositoryDao().findForUser(
//                    login
//                )
                val repos: List<Repository> = arrayListOf()

//                for ((newId, newName) in roomRepositories) {
//                    repos.last().run {
//                        id = newId
//                        name = newName
//                    }
//                }

                emitter.onSuccess(repos);
//            }
        }.subscribeOn(Schedulers.io()).cast((List::class.java) as Class<List<Repository>>)
    }
}

private fun Any.onSuccess() {
    TODO("Not yet implemented")
}
