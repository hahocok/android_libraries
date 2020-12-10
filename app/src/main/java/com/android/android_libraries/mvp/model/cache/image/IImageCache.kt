package com.android.android_libraries.mvp.model.cache.image

import android.graphics.Bitmap

import io.reactivex.Maybe
import io.reactivex.Single
import java.io.File

interface IImageCache {
    fun getFile(url: String): Maybe<File>
    fun saveImage(url: String, bitmap: Bitmap): Maybe<File>
    fun contains(url: String): Single<Boolean>
    fun clear()
    fun getImageDir(): File
    fun SHA1(s: String): String
    fun getSizeKb(): Float
    fun deleteFileOrDirRecursive(fileOrDirectory: File)
    fun getFileOrDirSize(f: File): Long
}