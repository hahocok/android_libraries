package com.android.android_libraries

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {
    enum class ButtonIndex(val pos: Int) {
        FIRST(0),
        SECOND(1),
        THIRD(2)
    }

    private val model: Model = Model(ButtonIndex.values().size)

    init {
        model.observable.subscribe {
            it.forEachIndexed { index, value ->
                when(index) {
                    ButtonIndex.FIRST.pos -> viewState.setTextFirstBtn(value)
                    ButtonIndex.SECOND.pos -> viewState.setTextSecondBtn(value)
                    ButtonIndex.THIRD.pos -> viewState.setTextThirdBtn(value)
                }
            }
        }
    }

    private fun calcValue(btn: ButtonIndex) {
        model.setAt(btn.pos, model.getAt(btn.pos) + 1)
    }

    fun onClickFirstBtn() {
        val currentBtn = ButtonIndex.FIRST
        calcValue(currentBtn)
        viewState.setTextFirstBtn(model.getAt(currentBtn.pos))
    }

    fun onClickSecondBtn() {
        val currentBtn = ButtonIndex.SECOND
        calcValue(currentBtn)
        viewState.setTextSecondBtn(model.getAt(currentBtn.pos))
    }

    fun onClickThirdBtn() {
        val currentBtn = ButtonIndex.THIRD
        calcValue(currentBtn)
        viewState.setTextThirdBtn(model.getAt(currentBtn.pos))
    }
}