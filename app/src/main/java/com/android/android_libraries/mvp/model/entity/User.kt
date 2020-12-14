package com.android.android_libraries.mvp.model.entity

import com.google.gson.annotations.Expose

data class User(
    @Expose
    val login: String,
    @Expose
    val avatar_url: String,
    @Expose
    val repos_url: String,
    @Expose
    val name: String
    )