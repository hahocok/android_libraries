package com.android.android_libraries.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.android.android_libraries.App
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.api.INetworkStatus.Status

class NetworkStatus : INetworkStatus {
    override fun getStatus(): Status {
        val cm: ConnectivityManager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo = cm.activeNetworkInfo ?: return Status.OFFLINE
        when (activeNetwork.type) {
            ConnectivityManager.TYPE_WIFI -> return Status.WIFI
            ConnectivityManager.TYPE_ETHERNET -> return Status.ETHERNET
            ConnectivityManager.TYPE_MOBILE -> return Status.MOBILE
        }
        return Status.OTHER
    }

    override fun isOnline(): Boolean {
        return getStatus() != Status.OFFLINE
    }

    override fun isWifi(): Boolean {
        return getStatus() == Status.WIFI
    }

    override fun isEthernet(): Boolean {
        return getStatus() == Status.ETHERNET
    }

    override fun isMobile(): Boolean {
        return getStatus() == Status.MOBILE
    }

    override fun isOffline(): Boolean {
        return getStatus() == Status.OFFLINE
    }
}