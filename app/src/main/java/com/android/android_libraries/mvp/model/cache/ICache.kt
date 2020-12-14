package com.android.android_libraries.mvp.model.cache

import android.graphics.Bitmap
import com.android.android_libraries.App
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

interface ICache {

    companion object {
        const val IMAGE_FOLDER_NAME = "avatar"
    }

    fun saveUser(login: String, user: User): Completable
    fun getUser(login: String): Single<User>

    fun saveUserRepository(user: User, repositories: MutableList<Repository>)
    fun getUserRepositories(login: String): Single<List<Repository>>

    fun saveImageAvatar(url: String, bitmap: Bitmap)
    fun getImageAvatar(url: String): Maybe<File>

    fun SHA1(s: String): String {
        val m: MessageDigest = MessageDigest.getInstance("SHA1")
        m.update(s.toByteArray(Charset.defaultCharset()), 0, s.length)
        return BigInteger(1, m.digest()).toString(16)
    }

    fun getImageDir(): File {
        return File(App.instance.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "/$IMAGE_FOLDER_NAME");
    }
}