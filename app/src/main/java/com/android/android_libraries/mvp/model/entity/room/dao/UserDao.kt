package com.android.android_libraries.mvp.model.entity.room.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.android.android_libraries.mvp.model.entity.room.RoomUser

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun insert(user: RoomUser)

    @Insert(onConflict = REPLACE)
    fun insert(vararg user: RoomUser)

    @Insert(onConflict = REPLACE)
    fun insert(user: List<RoomUser>)

    @Update
    fun update(user: RoomUser)

    @Update
    fun update(vararg user: RoomUser)

    @Update
    fun update(user: List<RoomUser>)

    @Delete
    fun delete(user: RoomUser)

    @Delete
    fun delete(vararg user: RoomUser)

    @Delete
    fun delete(user: List<RoomUser>)

    @Query("SELECT * FROM roomuser")
    fun getAll(): List<RoomUser>

    @Query("SELECT * FROM roomuser WHERE login = :login LIMIT 1")
    fun findByLogin(login: String?): RoomUser?

}