package com.android.android_libraries.mvp.model.cache

import android.graphics.Bitmap
import android.util.Log
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import io.paperdb.Paper
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.Action
import java.io.File
import java.io.FileOutputStream

class PaperCacheImpl : ICache {
    override fun saveUser(login: String, user: User): Completable =
        Completable.fromAction { Paper.book("users").write(login, user) }

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

        Paper.book("avatarPath").write(url, imageFile.toString())
    }

    override fun getImageAvatar(url: String): Maybe<File> {
        return Maybe.fromCallable {
            File(Paper.book("users").read(url) as String)
        }
    }
}