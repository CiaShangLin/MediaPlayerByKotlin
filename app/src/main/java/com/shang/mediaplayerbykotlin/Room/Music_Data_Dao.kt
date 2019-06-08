package com.shang.mediaplayerbykotlin.Room

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by Shang on 2018/8/14.
 */

@Dao
interface Music_Data_Dao {

    @Query("select * from " + Music_Data_Entity.TABLE_NAME)
    fun getAll(): LiveData<MutableList<Music_Data_Entity>>

    @Query("select * from " + Music_Data_Entity.TABLE_NAME + " where name= :fileName ")
    fun find_FileByName(fileName: String):Music_Data_Entity

    @Query("select name from "+Music_Data_Entity.TABLE_NAME)
    fun findAllName():MutableList<String>

    @Query("select * from "+Music_Data_Entity.TABLE_NAME+" where path=:path")
    fun findListData(path:String):Music_Data_Entity

    @Query("select * from " + Music_Data_Entity.TABLE_NAME+" ORDER BY name ASC")
    fun test(): MutableList<Music_Data_Entity>

    //vararg
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(musicEntity: Music_Data_Entity)

    @Update()
    fun update(musicEntity: Music_Data_Entity)    //找primary key去判斷的樣子 沒有賦予值的 會改回預設值

    @Delete()
    fun delete(musicEntity: Music_Data_Entity)

    @Delete()
    fun deleteALL(musicEntity: MutableList<Music_Data_Entity>)

}