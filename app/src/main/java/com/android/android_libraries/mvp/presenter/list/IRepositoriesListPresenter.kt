package com.android.android_libraries.mvp.presenter.list

import com.android.android_libraries.mvp.view.list.RepositoryItemView

interface IRepositoriesListPresenter {
    fun bind(view: RepositoryItemView)
    fun getCount(): Int
}