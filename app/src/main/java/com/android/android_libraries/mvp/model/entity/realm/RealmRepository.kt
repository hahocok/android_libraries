package com.android.android_libraries.mvp.model.entity.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmRepository : RealmObject() {
    operator fun component1(): String =
        id

    operator fun component2(): String =
        name

    @PrimaryKey
    var id: String = ""
    var name: String = ""
}