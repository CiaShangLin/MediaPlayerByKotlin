package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Shang on 2018/8/15.
 */
@Dao
interface Music_List_Dao {


    @Query("select * from " + Music_List_Entity.TABLE_NAME)
    fun getAll(): List<Music_List_Entity>

    @Insert
    fun insert(musicList:Music_List_Entity)

}