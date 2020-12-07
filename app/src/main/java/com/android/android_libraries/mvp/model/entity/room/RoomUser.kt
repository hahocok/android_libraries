package com.android.android_libraries.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomUser(@PrimaryKey val login: String,
                    var avatarUrl: String = "",
                    var reposUrl: String = "",
                    var name: String = "")