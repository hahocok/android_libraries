package com.android.android_libraries.mvp.model.repo.di

import com.android.android_libraries.di.module.ApiModule
import com.android.android_libraries.di.module.AppModule
import com.android.android_libraries.di.module.RepoModule
import com.android.android_libraries.mvp.model.repo.UsersRepoInstrumentedTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
(modules = [ AppModule::class, RepoModule::class, ApiModule::class])
interface TestInstrumentalComponent {
    fun inject(test: UsersRepoInstrumentedTest)
}