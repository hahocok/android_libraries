package com.android.android_libraries.di.module

import com.android.android_libraries.mvp.model.api.IDataSource
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.cache.ICache
import com.android.android_libraries.mvp.model.repo.IUsersRepo
import com.android.android_libraries.mvp.model.repo.UsersRepo
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
class RepoModule {

    @Singleton
    @Provides
    fun usersRepo(
        networkStatus: INetworkStatus,
        api: IDataSource,
        @Named("realm") cache: ICache
    ): IUsersRepo =
        UsersRepo(networkStatus, api, cache)

}