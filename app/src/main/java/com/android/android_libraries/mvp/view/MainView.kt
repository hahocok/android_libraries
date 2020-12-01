package com.android.android_libraries.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun init()
    fun updateList()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(text: String?)

    fun showLoading()
    fun hideLoading()

    fun setUsername(username: String?)
    fun loadImage(url: String?)
}