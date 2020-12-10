package com.android.android_libraries.di.module

import android.widget.ImageView
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.cache.image.IImageCache
import com.android.android_libraries.mvp.model.cache.image.RealmImageCache
import com.android.android_libraries.mvp.model.image.IImageLoader
import com.android.android_libraries.ui.image.GlideImageLoader
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
class ImageLoaderModule {

    @Named("realmI")
    @Provides
    fun realmImageCache(): IImageCache =
            RealmImageCache()

    @Singleton
    @Provides
    fun glideImageLoader(networkStatus: INetworkStatus, @Named("realmI") cache: IImageCache): IImageLoader<ImageView> =
        GlideImageLoader(networkStatus, cache)
}