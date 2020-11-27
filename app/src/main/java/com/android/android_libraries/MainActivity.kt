package com.android.android_libraries

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit


class MainActivity : MvpAppCompatActivity(), MainView {
    companion object {
        private const val INTERNAL_PATH_TO_MEDIA = "media"
        private const val FILE_NAME = "image_file.jpg"
        private const val FILE_NEW_NAME = "new_image_file.png"
    }

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private var disposableConverter: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        btnStart.setOnClickListener {
            presenter.clickStartConverter()
        }

        btnCancel.setOnClickListener {
            presenter.clickStopConverter()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelConverted()
    }

    override fun cancelConverted() {
        disposableConverter?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
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

    override fun startConverted(emitter: Emitter<Boolean>) {
        disposableConverter = Observable
            .fromCallable { assets.open(FILE_NAME) }
            .subscribeOn(Schedulers.io())
            .map { BitmapFactory.decodeStream(it) }
            .map {
                val folder = File(this.filesDir, INTERNAL_PATH_TO_MEDIA)
                if (!folder.exists()) {
                    folder.mkdir()
                }

                val newFile = File(folder, FILE_NEW_NAME)
                val out = FileOutputStream(newFile)
                val result = it.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.close()
                result
            }
            .delay(5 * 1000L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { emitter.onNext(it) },
                { emitter.onError(it) },
                { emitter.onComplete() }
            )
    }
}