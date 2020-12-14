package com.android.android_libraries

import android.app.Application
import androidx.multidex.MultiDex
import com.android.android_libraries.di.AppComponent
import com.android.android_libraries.di.DaggerAppComponent
import com.android.android_libraries.di.module.AppModule
import com.android.android_libraries.mvp.model.entity.room.db.Database
import io.paperdb.Paper
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var instance: App
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
        MultiDex.install(this)
        Paper.init(this)

        Database.create(this)

        Realm.init(this)
        val config: RealmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}