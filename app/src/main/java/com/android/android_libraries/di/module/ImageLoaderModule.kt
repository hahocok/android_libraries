package com.android.android_libraries.di.module

import android.widget.ImageView
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.cache.ICache
import com.android.android_libraries.mvp.model.image.IImageLoader
import com.android.android_libraries.ui.image.GlideImageLoader
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
class ImageLoaderModule {

    @Singleton
    @Provides
    fun glideImageLoader(networkStatus: INetworkStatus, @Named("realm") cache: ICache): IImageLoader<ImageView> =
        GlideImageLoader(networkStatus, cache)
}