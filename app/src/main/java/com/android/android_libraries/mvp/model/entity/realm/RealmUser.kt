package com.android.android_libraries.mvp.model.entity.realm

import androidx.room.PrimaryKey
import io.realm.RealmList
import io.realm.RealmObject

open class RealmUser : RealmObject() {
    @PrimaryKey var login: String = ""
    var avatarUrl: String = ""
    var reposUrl: String = ""
    var name: String = ""
    var repos: RealmList<RealmRepository> = RealmList()
}