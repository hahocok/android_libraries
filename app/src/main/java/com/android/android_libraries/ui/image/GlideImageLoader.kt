package com.android.android_libraries.ui.image

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.cache.ICache
import com.android.android_libraries.mvp.model.cache.image.IImageCache
import com.android.android_libraries.mvp.model.image.IImageLoader
import com.android.android_libraries.ui.NetworkStatus
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class GlideImageLoader(private var networkStatus: INetworkStatus, private val imageCache: IImageCache) : IImageLoader<ImageView> {

    companion object {
        private const val INTERNAL_PATH_TO_MEDIA = "media"
        private const val FILE_NAME = "image_file.jpg"
    }

    @SuppressLint("CheckResult")
    override fun loadInto(url: String, container: ImageView) {
        if (networkStatus.isOnline()) {
            Glide.with(container.context)
                    .asBitmap()
                    .load(url)
                    .addListener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            e?.printStackTrace()
                            return false
                        }

                        override fun onResourceReady(
                                resource: Bitmap?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            resource?.let { imageCache.saveImage(url, resource) }
                            return true
                        }
                    }).into(container)
        } else {
            imageCache.getFile(url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { file: File ->
                        Glide.with(container.context)
                                .load(file)
                                .into(container)
                    }

        }
    }
}