package com.android.android_libraries.mvp.model.cache

import android.graphics.Bitmap
import android.util.Log
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.mvp.model.entity.room.RoomRepository
import com.android.android_libraries.mvp.model.entity.room.RoomUser
import com.android.android_libraries.mvp.model.entity.room.db.Database
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream


class RoomCacheImpl : ICache {
    override fun saveUser(login: String, user: User): Completable {
        return Completable.fromAction {
            val roomUser = Database.getInstance().getUserDao().findByLogin(login)

            roomUser?.run {
                avatarUrl = user.avatar_url
                reposUrl = user.repos_url
                name = user.name
            }

            roomUser?.let { Database.getInstance().getUserDao().insert(it) }
        }
    }

    override fun getUser(login: String): Single<User> {
        return Single.create { emitter: SingleEmitter<Any?> ->
            val roomUser = Database.getInstance().getUserDao().findByLogin(login)
            if (roomUser == null) {
                emitter.onError(RuntimeException("No such user in cache"))
            } else {
                emitter.onSuccess(
                    User(
                        roomUser.login,
                        roomUser.avatarUrl,
                        roomUser.reposUrl,
                        roomUser.name
                    )
                )
            }
        }.subscribeOn(Schedulers.io()).cast(User::class.java)
    }

    override fun saveUserRepository(user: User, repositories: MutableList<Repository>) {
        var roomUser = Database.getInstance().getUserDao().findByLogin(user.login)
        if (roomUser == null) {
            roomUser = RoomUser(user.login, user.avatar_url, user.repos_url)
            Database.getInstance()
                .getUserDao()
                .insert(roomUser)
        }

        if (repositories.isNotEmpty()) {
            val roomRepositories: MutableList<RoomRepository> = ArrayList()
            for (repository in repositories) {
                val roomRepository =
                    RoomRepository(repository.id, repository.name, user.login)
                roomRepositories.add(roomRepository)
            }
            Database.getInstance()
                .getRepositoryDao()
                .insert(roomRepositories)
        }
    }

    override fun getUserRepositories(login: String): Single<List<Repository>> {
        return Single.create { emitter: SingleEmitter<Any?> ->
            val roomUser = Database.getInstance().getUserDao().findByLogin(login)
            roomUser?.let {
                val roomRepositories: List<RoomRepository> =
                    Database.getInstance().getRepositoryDao().findForUser(login)

                val repos: List<Repository> = arrayListOf()

                for ((newId, newName) in roomRepositories) {
                    repos.last().run {
                        id = newId
                        name = newName
                    }
                }

                emitter.onSuccess(repos);
            } ?: emitter.onError(RuntimeException("No such user in cache"))

        }.subscribeOn(Schedulers.io()).cast((List::class.java) as Class<List<Repository>>)
    }

    override fun saveImageAvatar(url: String, bitmap: Bitmap) {
        if (!getImageDir().exists() && !getImageDir().mkdirs()) {
            throw RuntimeException("Failed to create directory: " + getImageDir().toString())
        }

        val fileFormat = if (url.contains(".jpg")) ".jpg" else ".png"
        val imageFile = File(getImageDir(), SHA1(url) + fileFormat)
        val fos: FileOutputStream

        try {
            fos = FileOutputStream(imageFile)
            bitmap.compress(
                if (fileFormat == "jpg") Bitmap.CompressFormat.JPEG else Bitmap.CompressFormat.PNG,
                100,
                fos
            )
            fos.close()
        } catch (e: Exception) {
            Log.e("TAG", e.stackTraceToString())
        }

        val roomUser = Database.getInstance().getUserDao().findByUrl(url)
        roomUser.avatarPath = imageFile.toString()
        Database.getInstance().getUserDao().insert(roomUser)
    }

    override fun getImageAvatar(url: String): Maybe<File> {
        return Maybe.fromCallable {
            val roomUser = Database.getInstance().getUserDao().findByUrl(url)
            File(roomUser.avatarPath)
        }
    }
}
