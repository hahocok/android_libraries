package com.android.android_libraries

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.android.android_libraries.di.AppComponent
import com.android.android_libraries.di.DaggerAppComponent
import com.android.android_libraries.di.module.AppModule
import io.paperdb.Paper
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    companion object {
        lateinit var instance: App
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        Paper.init(this)

//        Database.create(this)

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