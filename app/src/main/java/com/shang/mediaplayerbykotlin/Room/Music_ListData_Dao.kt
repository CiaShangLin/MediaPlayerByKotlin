package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface Music_ListData_Dao {


    @Query("select * from "+Music_ListData_Entity.TABLE_NAME)
    fun getAll_ListData()

    @Insert
    fun insert(musicListData:Music_ListData_Entity)

}