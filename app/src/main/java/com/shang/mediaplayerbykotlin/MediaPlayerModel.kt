package com.shang.mediaplayerbykotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.Setting_Entity

class MediaPlayerModel(application: Application) : AndroidViewModel(application) {

    private var musicRepository = MusicRepository(application)
    private var settingRepository = SettingRepository(application)

    fun getAllMusicData(): LiveData<MutableList<Music_Data_Entity>> {
        return musicRepository.getAllMusicData()
    }

    fun find_FileByName(fileName: String):Music_Data_Entity{
        return musicRepository.find_FileByName(fileName)
    }

    fun findAllName():MutableList<String>{
        return musicRepository.findAllName()
    }

    fun findListData(path:String):Music_Data_Entity{
        return musicRepository.findListData(path)
    }

    fun insert(musicEntity: Music_Data_Entity){
        musicRepository.insert(musicEntity)
    }

    fun update(musicEntity: Music_Data_Entity){
        musicRepository.update(musicEntity)
    }

    fun delete(musicEntity: Music_Data_Entity){
        musicRepository.delete(musicEntity)
    }

    fun deleteALL(musicEntity: MutableList<Music_Data_Entity>){
        musicRepository.deleteALL(musicEntity)
    }

    fun getSettingLiveData(): LiveData<Setting_Entity> {
        return settingRepository.getSettingEntiny()
    }

    fun updateSetting(setting_Entity: Setting_Entity) {
        settingRepository.updateSetting(setting_Entity)
    }


}