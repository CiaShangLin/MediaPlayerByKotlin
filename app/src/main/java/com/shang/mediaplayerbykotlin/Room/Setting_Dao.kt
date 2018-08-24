package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

/**
 * Created by SERS on 2018/8/23.
 */
@Dao
interface Setting_Dao {

    @Query("select * from " + Setting_Entity.TABLE_NAME)
    fun getSetting(): Setting_Entity

    @Insert
    fun insertSetting(setting_Entity: Setting_Entity)

    @Update
    fun update(setting_Entity: Setting_Entity)

}