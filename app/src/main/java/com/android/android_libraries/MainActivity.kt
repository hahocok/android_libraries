package com.android.android_libraries

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        btnCounter1.setOnClickListener { presenter.onClickFirstBtn() }
        btnCounter2.setOnClickListener { presenter.onClickSecondBtn() }
        btnCounter3.setOnClickListener { presenter.onClickThirdBtn() }
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