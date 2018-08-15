package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Shang on 2018/8/14.
 */

@Dao
interface MusicDao {

    @Query("select * from " + Music_Data_Entity.TABLE_NAME)
    fun getAll(): List<Music_Data_Entity>

    @Insert
    fun insert(musicEntity: Music_Data_Entity)

}