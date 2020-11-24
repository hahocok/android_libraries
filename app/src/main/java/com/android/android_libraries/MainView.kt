package com.android.android_libraries

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView: MvpView {
    fun setTextFirstBtn(count: Int)
    fun setTextSecondBtn(count: Int)
    fun setTextThirdBtn(count: Int)
}