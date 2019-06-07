package com.shang.mediaplayerbykotlin.Room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

/**
 * Created by SERS on 2018/8/23.
 */
@Dao
interface Setting_Dao {

    @Query("select * from " + Setting_Entity.TABLE_NAME)
    fun getSetting(): LiveData<Setting_Entity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSetting(setting_Entity: Setting_Entity)

    @Update
    fun update(setting_Entity: Setting_Entity)

}