package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.*

/**
 * Created by Shang on 2018/8/14.
 */

@Dao
interface Music_Data_Dao {

    @Query("select * from " + Music_Data_Entity.TABLE_NAME)
    fun getAll(): MutableList<Music_Data_Entity>

    @Query("select * from " + Music_Data_Entity.TABLE_NAME + " where name= :fileName ")
    fun find_FileByName(fileName: String):Music_Data_Entity


    @Query("select name from "+Music_Data_Entity.TABLE_NAME)
    fun findAllName():MutableList<String>

    @Query("")
    fun findByName(name:String):Boolean{
        if(find_FileByName(name)!=null){
            return true
        }
        return false
    }

    //vararg
    @Insert()
    fun insert(musicEntity: Music_Data_Entity)

    @Update()
    fun update(musicEntity: Music_Data_Entity)    //找primary key去判斷的樣子 沒有賦予值的 會改回預設值

    @Delete()
    fun delete(musicEntity: Music_Data_Entity)

    @Delete()
    fun deleteALL(musicEntity: MutableList<Music_Data_Entity>)

}