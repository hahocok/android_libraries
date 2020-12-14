package com.android.android_libraries.mvp.presenter.di

import com.android.android_libraries.mvp.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestRepoModule::class])
interface TestComponent {
    fun inject(presenter: MainPresenter)
}