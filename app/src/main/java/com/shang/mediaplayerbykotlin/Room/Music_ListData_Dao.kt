package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.*

@Dao
interface Music_ListData_Dao {


    @Query("select * from "+Music_ListData_Entity.TABLE_NAME)
    fun getAll_ListData():MutableList<Music_ListData_Entity>


    @Query("select * from "+Music_ListData_Entity.TABLE_NAME+" where table_id=:id order by position")
    fun getListDataFromListName(id:Long):MutableList<Music_ListData_Entity>


    @Insert
    fun insert(musicListData:Music_ListData_Entity)

    @Update
    fun update(musicListData:Music_ListData_Entity)

    @Delete
    fun delete(musicListData:Music_ListData_Entity)

}