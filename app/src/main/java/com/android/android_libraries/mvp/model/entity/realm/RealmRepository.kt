package com.android.android_libraries.mvp.model.entity.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class RealmRepository(@PrimaryKey var id: String,
                           var name: String)
    : RealmObject()