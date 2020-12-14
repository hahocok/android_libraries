package com.android.android_libraries.di.module

import android.content.Context
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.cache.ICache
import com.android.android_libraries.mvp.model.cache.PaperCacheImpl
import com.android.android_libraries.mvp.model.cache.RealmCacheImpl
import com.android.android_libraries.mvp.model.cache.RoomCacheImpl
import com.android.android_libraries.ui.NetworkStatus
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private var context: Context) {

    @Singleton
    @Provides
    fun getContext(): Context =
        context

    @Named("realm")
    @Singleton
    @Provides
    fun getRealmCache(): ICache =
        RealmCacheImpl()

    @Named("paper")
    @Singleton
    @Provides
    fun getPaperCache(): ICache =
        PaperCacheImpl()

    @Named("room")
    @Singleton
    @Provides
    fun getRoomCache(): ICache =
        RoomCacheImpl()

    @Singleton
    @Provides
    fun getNetworkStatus(): INetworkStatus =
        NetworkStatus(context)

}