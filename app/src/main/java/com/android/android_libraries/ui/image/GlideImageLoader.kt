package com.android.android_libraries.ui.image

import android.widget.ImageView
import com.android.android_libraries.mvp.model.image.IImageLoader
import com.bumptech.glide.Glide

class GlideImageLoader : IImageLoader<ImageView> {
    override fun loadInto(url: String?, container: ImageView) {
        Glide.with(container.context)
            .asBitmap()
            .load(url)
            .into(container)
    }
}