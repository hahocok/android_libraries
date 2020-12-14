package com.android.android_libraries.mvp.model.entity.realm

import io.realm.RealmObject

open class RealmImage: RealmObject() {

    var url: String = ""
    var path: String = ""
}