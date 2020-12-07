package com.android.android_libraries.mvp.model.cache

import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.mvp.model.entity.realm.RealmRepository
import com.android.android_libraries.mvp.model.entity.realm.RealmUser
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
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
                realmUser.avatarUrl = user.avatar_url
                realmUser.reposUrl = user.repos_url
            }
        }
        realm.close()
    }

    override fun getUser(login: String): Single<User> {
        val realm: Realm = Realm.getDefaultInstance()
        val realmUser: RealmUser? =
            realm.where(RealmUser::class.java).equalTo("login", login).findFirst()
        val user: User
        user = if (realmUser == null) {
            return Single.error<Any>(RuntimeException("no such user in cache")) as Single<User>
        } else {
            User(realmUser.login,
                realmUser.avatarUrl,
                realmUser.reposUrl,
                realmUser.name
            )
        }
        realm.close()

        return Single.fromCallable { user }
            .subscribeOn(Schedulers.io())
            .cast(User::class.java)

    }

    override fun saveUserRepository(user: User, repositories: MutableList<Repository>) {
        val realm: Realm = Realm.getDefaultInstance()
        val realmUser: RealmUser? =
            realm.where(RealmUser::class.java).equalTo("login", user.login).findFirst()

        if (realmUser == null) {
            realm.executeTransaction { innerRealm ->
                val newRealmUser: RealmUser =
                    innerRealm.createObject(RealmUser::class.java, user.login)
                newRealmUser.avatarUrl = user.avatar_url
                newRealmUser.reposUrl = user.repos_url
            }
        }

        realm.executeTransaction { innerRealm ->
            realmUser?.repos?.deleteAllFromRealm()
            realmUser?.repos?.forEach(Consumer { repository ->
                val realmRepository: RealmRepository =
                    innerRealm.createObject(RealmRepository::class.java, repository.id)
                realmRepository.name = repository.name
                realmUser.repos.add(realmRepository)
            })
        }
        realm.close()
    }

    override fun getUserRepositories(login: String): Single<List<Repository>> {
        val realm = Realm.getDefaultInstance()
        val realmUser = realm.where(RealmUser::class.java).equalTo("login", login).findFirst()
        val repos: MutableList<Repository> = ArrayList()
        if (realmUser == null) {
            return Single.error<Any>(java.lang.RuntimeException("no repos for such user in cache")) as Single<List<Repository>>
        } else {
            for ((id, name) in realmUser.repos) {
                repos.add(Repository(id, name))
            }
        }
        realm.close()
        return Single.fromCallable<List<Repository>> { repos }
            .subscribeOn(Schedulers.io()).cast(MutableList::class.java as Class<List<Repository>>)
    }
}