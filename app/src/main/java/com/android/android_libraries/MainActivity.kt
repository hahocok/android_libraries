package com.android.android_libraries

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.android_libraries.databinding.ActivityMainBinding
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*

const val INTERNAL_PATH_TO_MEDIA = "media"
const val FILE_NAME = "image_file.jpg"
const val FILE_NEW_NAME = "new_image_file.png"

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.initView(this, binding)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancelConverted()
    }

    override fun showProgress() {
        progressView.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressView.visibility = View.GONE
    }

    override fun showStartBtn() {
        btnStart.visibility = View.VISIBLE
    }

    override fun showCancelBtn() {
        btnCancel.visibility = View.VISIBLE
    }

    override fun hideStartBtn() {
        btnStart.visibility = View.GONE
    }

    override fun hideCancelBtn() {
        btnCancel.visibility = View.GONE
    }

    override fun setTextProgress() {
        titleView.setText(R.string.converter_progress)
    }

    override fun setTextStart() {
        titleView.setText(R.string.converter_jpg_to_png)
    }

    override fun showSuccess() {
        Toast.makeText(this, R.string.convert_success, Toast.LENGTH_SHORT).show()
    }

    override fun showCancelMsg() {
        Toast.makeText(this, R.string.convert_was_canceled, Toast.LENGTH_SHORT).show()
    }

    override fun showError(e: Throwable) {
        Toast.makeText(this, R.string.convert_error, Toast.LENGTH_SHORT).show()
    }
}