package com.android.android_libraries

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class ConverterImpl(var context: Context, var emitter: Emitter<Boolean>) : IConverter {

    var disposableConverter: Disposable? = null

    override fun startConverted() {
        disposableConverter = Observable
                .fromCallable { context.assets.open(FILE_NAME) }
                .subscribeOn(Schedulers.io())
                .map { BitmapFactory.decodeStream(it) }
                .map {
                    val folder = File(context.filesDir, INTERNAL_PATH_TO_MEDIA)
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

    override fun cancelConverted() {
        disposableConverter?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}