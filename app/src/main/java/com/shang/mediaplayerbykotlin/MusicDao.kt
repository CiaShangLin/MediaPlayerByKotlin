package com.shang.mediaplayerbykotlin

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Shang on 2018/8/14.
 */

@Dao
interface MusicDao {

    @Query("select * from " + MusicData.TABLE_NAME)
    fun getAll(): List<MusicData>

    @Insert
    fun insert(musicData: MusicData)
}