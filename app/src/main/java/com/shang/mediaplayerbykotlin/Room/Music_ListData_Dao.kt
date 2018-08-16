package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert

@Dao
interface Music_ListData_Dao {


    @Insert
    fun insert(musicListData:Music_ListData_Entity)

}