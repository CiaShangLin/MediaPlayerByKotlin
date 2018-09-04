package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.*

/**
 * Created by Shang on 2018/8/15.
 */
@Dao
interface Music_ListName_Dao {


    @Query("select * from " + Music_ListName_Entity.TABLE_NAME)
    fun getAll(): MutableList<Music_ListName_Entity>

    @Query("select tableName from " + Music_ListName_Entity.TABLE_NAME)
    fun getAllTableName(): Array<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(musicList:Music_ListName_Entity)

    @Delete
    fun delete(musicList:Music_ListName_Entity)

    @Update
    fun update(musicList:Music_ListName_Entity)


}