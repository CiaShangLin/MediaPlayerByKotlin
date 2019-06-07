package com.shang.mediaplayerbykotlin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.RoomDatabase
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Setting_Entity

class SettingRepository(context: Context) {


    private val settingDao by lazy {
        MusicDatabase.getMusicDatabase(context).getSetting_Dao()
    }

    fun getSettingEntiny():LiveData<Setting_Entity>{
        return settingDao.getSetting()
    }

    fun updateSetting(setting_Entity: Setting_Entity){
        settingDao.update(setting_Entity)
    }



}