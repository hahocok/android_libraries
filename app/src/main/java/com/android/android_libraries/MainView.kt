package com.android.android_libraries

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SingleStateStrategy::class)
interface MainView: MvpView {
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showProgress()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun hideProgress()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showStartBtn()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showCancelBtn()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun hideStartBtn()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun hideCancelBtn()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun setTextProgress()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun setTextStart()
    @StateStrategyType(value = SkipStrategy::class)
    fun showSuccess()
    @StateStrategyType(value = SkipStrategy::class)
    fun showCancelMsg()
    @StateStrategyType(value = SkipStrategy::class)
    fun showError(e: Throwable)
}