package com.android.android_libraries.mvp.presenter.di

import com.android.android_libraries.mvp.model.repo.IUsersRepo
import com.android.android_libraries.mvp.model.repo.UsersRepo
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
open class TestRepoModule {

    @Singleton
    @Provides
    open fun usersRepo(): IUsersRepo =
        Mockito.mock(UsersRepo::class.java)
}