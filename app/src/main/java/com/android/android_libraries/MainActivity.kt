package com.android.android_libraries

import android.annotation.SuppressLint
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    @SuppressLint("CheckResult")
    private fun initView() {
        btnCounter1.setOnClickListener { presenter.onClickFirstBtn() }
        btnCounter2.setOnClickListener { presenter.onClickSecondBtn() }
        btnCounter3.setOnClickListener { presenter.onClickThirdBtn() }

        RxTextView.textChanges(editTextView)
            .subscribe {
                textView.text = it
            }
    }

    override fun setTextFirstBtn(count: Int) {
        btnCounter1.text = getString(R.string.count_format, count)
    }

    override fun setTextSecondBtn(count: Int) {
        btnCounter2.text = getString(R.string.count_format, count)
    }

    override fun setTextThirdBtn(count: Int) {
        btnCounter3.text = getString(R.string.count_format, count)
    }
}