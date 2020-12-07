package com.android.android_libraries

import android.app.Application
import androidx.multidex.MultiDex
import com.android.android_libraries.mvp.model.entity.room.db.Database
import io.paperdb.Paper
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        Paper.init(this)

        Database.create(this)

        Realm.init(this)
        val config: RealmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}