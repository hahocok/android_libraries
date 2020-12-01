package com.android.android_libraries.mvp.view.list

interface RepositoryItemView {
    fun getCurrentPos(): Int
    fun setName(name: String?)
}