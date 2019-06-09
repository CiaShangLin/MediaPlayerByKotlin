package com.shang.mediaplayerbykotlin.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Setting_Entity

class SettingRepository(context: Context) {


    private val settingDao by lazy {
        MusicDatabase.getMusicDatabase(context).getSetting_Dao()
    }

    fun getSettingNow():Setting_Entity{
        return settingDao.getSettingNow()
    }

    fun getSettingLiveData():LiveData<Setting_Entity>{
        return settingDao.getSettingLiveData()
    }

    fun insertSetting(setting_Entity: Setting_Entity){
        settingDao.insertSetting(setting_Entity)
    }

    fun updateSetting(setting_Entity: Setting_Entity){
        settingDao.update(setting_Entity)
    }



}