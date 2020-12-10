package com.android.android_libraries.mvp.model.entity.room.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.android.android_libraries.mvp.model.entity.room.RoomRepository

//@Dao
interface RepositoryDao {

    @Insert(onConflict = REPLACE)
    fun insert(user: RoomRepository)

    @Insert(onConflict = REPLACE)
    fun insert(vararg user: RoomRepository)

    @Insert(onConflict = REPLACE)
    fun insert(user: List<RoomRepository>)

    @Update
    fun update(user: RoomRepository)

    @Update
    fun update(vararg user: RoomRepository)

    @Update
    fun update(user: List<RoomRepository>)

    @Delete
    fun delete(user: RoomRepository)

    @Delete
    fun delete(vararg user: RoomRepository)

    @Delete
    fun delete(user: List<RoomRepository>)

    @Query("SELECT * FROM roomrepository")
    fun getAll(): List<RoomRepository>

    @Query("SELECT * FROM roomrepository WHERE userLogin = :login")
    fun findForUser(login: String): List<RoomRepository>
}