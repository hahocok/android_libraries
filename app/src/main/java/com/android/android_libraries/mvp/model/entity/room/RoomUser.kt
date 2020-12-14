package com.android.android_libraries.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomUser(@PrimaryKey
               var login: String = "",
               var avatarUrl: String = "",
               var reposUrl: String = "",
               var name: String = "",
               var avatarPath: String = "")