package com.android.android_libraries

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Emitter

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {

    override fun attachView(view: MainView?) {
        super.attachView(view)

        resetView()
    }

    fun clickStartConverter() {
        viewState.showProgress()
        viewState.hideStartBtn()
        viewState.showCancelBtn()
        viewState.setTextProgress()

        viewState.startConverted(object : Emitter<Boolean> {
            override fun onComplete() {
            }

            override fun onNext(value: Boolean) {
                viewState.showSuccess()
                resetView()
            }

            override fun onError(error: Throwable) {
                viewState.showError(error)
                resetView()
            }
        })
    }

    fun clickStopConverter() {
        viewState.cancelConverted()
        viewState.showCancelMsg()
        resetView()
    }

    private fun resetView() {
        viewState.setTextStart()
        viewState.hideProgress()
        viewState.showStartBtn()
        viewState.hideCancelBtn()
    }
}