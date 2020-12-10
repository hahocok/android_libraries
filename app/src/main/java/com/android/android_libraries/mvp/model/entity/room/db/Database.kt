package com.android.android_libraries.mvp.model.entity.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.android_libraries.mvp.model.entity.room.RoomRepository
import com.android.android_libraries.mvp.model.entity.room.RoomUser
import com.android.android_libraries.mvp.model.entity.room.dao.RepositoryDao
import com.android.android_libraries.mvp.model.entity.room.dao.UserDao


//@Database(entities = [RoomUser::class, RoomRepository::class], version = 1, exportSchema = false)
//abstract class Database : RoomDatabase() {
//    companion object{
//
//        private val DB_NAME = "userDatabase.db"
//
//        @Volatile
//        private lateinit var instance: com.android.android_libraries.mvp.model.entity.room.db.Database
//
//        @Synchronized
//        fun getInstance(): com.android.android_libraries.mvp.model.entity.room.db.Database {
//            return instance
//        }
//
//        fun create(context: Context) {
//            instance = Room.databaseBuilder(
//                context,
//                com.android.android_libraries.mvp.model.entity.room.db.Database::class.java, DB_NAME
//            ).build()
//        }
//    }
//
//    abstract fun getUserDao(): UserDao
//    abstract fun getRepositoryDao(): RepositoryDao
//
//}