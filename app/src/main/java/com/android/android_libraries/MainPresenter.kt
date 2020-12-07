package com.android.android_libraries

import android.content.Context
import com.android.android_libraries.databinding.ActivityMainBinding
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Emitter

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {

    private lateinit var converter: IConverter

    override fun attachView(view: MainView?) {
        super.attachView(view)

        resetView()
    }

    private fun clickStartConverter(mainContext: Context) {
        viewState.showProgress()
        viewState.hideStartBtn()
        viewState.showCancelBtn()
        viewState.setTextProgress()

        converter = ConverterImpl(mainContext, object : Emitter<Boolean> {
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

    private fun clickStopConverter() {
        converter.cancelConverted()
        viewState.showCancelMsg()
        resetView()
    }

    private fun resetView() {
        viewState.setTextStart()
        viewState.hideProgress()
        viewState.showStartBtn()
        viewState.hideCancelBtn()
    }

    fun initView(mainContext: Context, binding: ActivityMainBinding) {
        binding.btnStart.setOnClickListener {
            clickStartConverter(mainContext)
        }

        binding.btnCancel.setOnClickListener {
            clickStopConverter()
        }
    }

    fun cancelConverted() {
        converter.cancelConverted()
    }
}