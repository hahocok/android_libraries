package com.android.android_libraries.mvp.model.cache.image

import android.graphics.Bitmap
import android.util.Log
import com.android.android_libraries.App
import com.android.android_libraries.mvp.model.entity.realm.RealmImage
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.MaybeOnSubscribe
import io.reactivex.Single
import io.realm.Realm
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest


class RealmImageCache : IImageCache {
    private val IMAGE_FOLDER_NAME = "image"

    override fun getFile(url: String): Maybe<File> {
        return Maybe.fromCallable {
            val cachedImage: RealmImage? =
                Realm.getDefaultInstance().where(RealmImage::class.java)
                    .equalTo("url", url).findFirst()
            if (cachedImage != null) {
                return@fromCallable File(cachedImage.path)
            }
            null
        }

    }

    override fun saveImage(url: String, bitmap: Bitmap): Maybe<File> {
        if (!getImageDir().exists() && !getImageDir().mkdirs()) {
            throw RuntimeException("Failed to create directory: " + getImageDir().toString())
        }

        val fileFormat = if (url.contains(".jpg")) ".jpg" else ".png"
        val imageFile = File(getImageDir(), SHA1(url) + fileFormat)
        val fos: FileOutputStream

        try {
            fos = FileOutputStream(imageFile)
            bitmap.compress(
                if (fileFormat == "jpg") Bitmap.CompressFormat.JPEG else Bitmap.CompressFormat.PNG,
                100,
                fos
            )
            fos.close()
        } catch (e: Exception) {
            Log.e("TAG", e.stackTraceToString())
            return Maybe.create {}
        }

        return Maybe.create { emitter: MaybeEmitter<File> ->
            Realm.getDefaultInstance().executeTransactionAsync(
                { realm: Realm ->
                    val cachedImage = RealmImage()
                    cachedImage.url = url
                    cachedImage.path = imageFile.toString()
                    realm.copyToRealm(cachedImage)
                },
                { emitter.onSuccess(imageFile) }
            ) { error: Throwable? -> emitter.onComplete() }
        }

    }

    override fun contains(url: String): Single<Boolean> {
        return Single.fromCallable {
            Realm.getDefaultInstance().where(
                RealmImage::class.java
            ).equalTo("url", url).count() > 0
        }
    }

    override fun clear() {
        Realm.getDefaultInstance().executeTransaction { realm: Realm ->
            realm.delete(
                RealmImage::class.java
            )
        }
        deleteFileOrDirRecursive(getImageDir())
    }

    override fun getImageDir(): File {
        return File(App.instance.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "/$IMAGE_FOLDER_NAME");
    }

    override fun SHA1(s: String): String {
        val m: MessageDigest = MessageDigest.getInstance("SHA1")
        m.update(s.toByteArray(Charset.defaultCharset()), 0, s.length)
        val out: String = BigInteger(1, m.digest()).toString(16)
        return out

    }

    override fun getSizeKb(): Float =
        getFileOrDirSize(getImageDir()) / 1024f

    override fun deleteFileOrDirRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            fileOrDirectory.listFiles()?.forEach {
                deleteFileOrDirRecursive(it)
            }
        }
        fileOrDirectory.delete()
    }

    override fun getFileOrDirSize(f: File): Long {
        var size: Long = 0
        if (f.isDirectory) {
            f.listFiles()?.forEach {
                size += getFileOrDirSize(it)
            }
        } else {
            size = f.length()
        }
        return size

    }
}