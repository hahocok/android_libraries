package com.android.android_libraries.ui.image

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.ImageView
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.cache.ICache
import com.android.android_libraries.mvp.model.image.IImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File


class GlideImageLoader(private var networkStatus: INetworkStatus, private val cache: ICache) : IImageLoader<ImageView> {

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
                            resource?.let { cache.saveImageAvatar(url, resource) }
                            return false
                        }
                    }).into(container)
        } else {
            cache.getImageAvatar(url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { file: File ->
                        Glide.with(container.context)
                                .load(file)
                                .into(container)
                    }

        }
    }
}