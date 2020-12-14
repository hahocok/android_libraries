package com.android.android_libraries.di

import com.android.android_libraries.di.module.ApiModule
import com.android.android_libraries.di.module.AppModule
import com.android.android_libraries.di.module.ImageLoaderModule
import com.android.android_libraries.di.module.RepoModule
import com.android.android_libraries.mvp.presenter.MainPresenter
import com.android.android_libraries.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RepoModule::class,
    AppModule::class,
    ApiModule::class,
    ImageLoaderModule::class
])
interface AppComponent {
    fun inject(presenter: MainPresenter)
    fun inject(mainActivity: MainActivity)
}