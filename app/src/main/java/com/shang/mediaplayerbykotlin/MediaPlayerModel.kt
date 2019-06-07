package com.shang.mediaplayerbykotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shang.mediaplayerbykotlin.Room.Setting_Entity

class MediaPlayerModel(application: Application) : AndroidViewModel(application) {


    private var settingRepository = SettingRepository(application)

    fun getSettingLiveData(): LiveData<Setting_Entity> {
        return settingRepository.getSettingEntiny()
    }

    fun updateSetting(setting_Entity: Setting_Entity){
        settingRepository.updateSetting(setting_Entity)
    }



}