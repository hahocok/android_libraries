package com.android.android_libraries.mvp.model.cache

import android.graphics.Bitmap
import android.util.Log
import com.android.android_libraries.App
import com.android.android_libraries.mvp.model.cache.ICache.Companion.IMAGE_FOLDER_NAME
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.mvp.model.entity.realm.RealmRepository
import com.android.android_libraries.mvp.model.entity.realm.RealmUser
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.function.Consumer


class RealmCacheImpl : ICache {

    override fun saveUser(login: String, user: User) {
        val realm: Realm = Realm.getDefaultInstance()
        val realmUser: RealmUser? =
            realm.where(RealmUser::class.java).equalTo("login", login).findFirst()

        if (realmUser == null) {
            realm.executeTransaction { innerRealm ->
                val newRealmUser: RealmUser = innerRealm.createObject(RealmUser::class.java, login)
                newRealmUser.avatarUrl = user.avatar_url
                newRealmUser.reposUrl = user.repos_url
            }
        } else {
            realm.executeTransaction { innerRealm ->
                val newRealmUser: RealmUser? =
                    innerRealm.where(RealmUser::class.java)
                        .equalTo("login", login).findFirst()
                newRealmUser?.avatarUrl = user.avatar_url
                newRealmUser?.reposUrl = user.repos_url
            }
        }
        realm.close()
    }

    override fun getUser(login: String): Single<User> {
        val realm: Realm = Realm.getDefaultInstance()

        val realmUser: RealmUser? =
            realm.where(RealmUser::class.java).equalTo("login", login).findFirst()?.let {
                realm.copyFromRealm(it) }
        realm.close()

        realmUser?.let {
            println(it.login)
            val user = User(it.login,
                it.avatarUrl,
                it.reposUrl,
                it.name
            )
            return Single.fromCallable { user }
                .subscribeOn(Schedulers.io())
                .cast(User::class.java)
        } ?: return Single.error<Any>(RuntimeException("no such user in cache")) as Single<User>
    }

    override fun saveUserRepository(user: User, repositories: MutableList<Repository>) {
        val realm: Realm = Realm.getDefaultInstance()
        val realmUser: RealmUser? =
            realm.where(RealmUser::class.java).equalTo("login", user.login).findFirst()

        realm.executeTransaction { innerRealm ->
            val newRealmUser: RealmUser =
                innerRealm.createObject(RealmUser::class.java, user.login)
            newRealmUser.avatarUrl = user.avatar_url
            newRealmUser.reposUrl = user.repos_url
        }

        realm.executeTransaction { innerRealm ->
            realmUser?.repos?.deleteAllFromRealm()
            val realmRepositoryList = ArrayList<RealmRepository>()

            repositories.forEach(Consumer { repository ->
                val realmRepository: RealmRepository =
                    innerRealm.createObject(RealmRepository::class.java, repository.id)
                realmRepository.name = repository.name
                realmRepositoryList.add(realmRepository)
            })

            realmUser?.repos?.addAll(realmRepositoryList)
        }
        realm.close()
    }

    override fun getUserRepositories(login: String): Single<List<Repository>> {
        val realm = Realm.getDefaultInstance()
        val realmUser = realm.where(RealmUser::class.java).equalTo("login", login).findFirst()
        val repos: MutableList<Repository> = ArrayList()

        realmUser?.repos?.forEach { (id, name) ->
            repos.add(Repository(id, name))
        } ?: return Single.error<Any>(java.lang.RuntimeException("no repos for such user in cache")) as Single<List<Repository>>

        realm.close()
        return Single.fromCallable<List<Repository>> { repos }
            .subscribeOn(Schedulers.io()).cast(MutableList::class.java as Class<List<Repository>>)
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

        Realm.getDefaultInstance().executeTransactionAsync { realm: Realm ->
            val realmUser = realm.where(RealmUser::class.java).equalTo("avatarUrl", url).findFirst()
            realmUser?.avatarPath = imageFile.toString()
            realmUser?.let { realm.copyToRealm(it) }
        }
    }

    override fun getImageAvatar(url: String): Maybe<File> {
        return Maybe.fromCallable {
            val realm = Realm.getDefaultInstance()
            val realmUser = realm.where(RealmUser::class.java).equalTo("avatarUrl", url).findFirst()
            realmUser?.avatarPath?.let { File(it) }
        }
    }
}