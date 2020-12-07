package com.android.android_libraries.mvp.model.api

interface INetworkStatus {
    enum class Status{
        WIFI,
        MOBILE,
        ETHERNET,
        OTHER,
        OFFLINE
    }

    fun getStatus() : Status
    fun isOnline() : Boolean
    fun isWifi() : Boolean
    fun isEthernet() : Boolean
    fun isMobile() : Boolean
    fun isOffline() : Boolean
}