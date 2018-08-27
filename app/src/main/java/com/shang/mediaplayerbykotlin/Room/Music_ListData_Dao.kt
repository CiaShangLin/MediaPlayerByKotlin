package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface Music_ListData_Dao {


    @Query("select * from "+Music_ListData_Entity.TABLE_NAME)
    fun getAll_ListData():MutableList<Music_ListData_Entity>


    @Query("select * from "+Music_ListData_Entity.TABLE_NAME+" where table_id=:id")
    fun getListDataFromListName(id:Long):MutableList<Music_ListData_Entity>


    @Insert
    fun insert(musicListData:Music_ListData_Entity)

}