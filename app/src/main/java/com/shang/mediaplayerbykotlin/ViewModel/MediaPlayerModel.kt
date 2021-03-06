package com.shang.mediaplayerbykotlin.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.shang.mediaplayerbykotlin.Repository.MusicRepository
import com.shang.mediaplayerbykotlin.Repository.PlayListNameRepository
import com.shang.mediaplayerbykotlin.Repository.SettingRepository
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.Music_ListName_Entity
import com.shang.mediaplayerbykotlin.Room.Setting_Entity

class MediaPlayerModel(application: Application) : AndroidViewModel(application) {

    private var loadStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    private var musicRepository = MusicRepository(application)
    private var settingRepository = SettingRepository(application)
    private var playListRepository = PlayListNameRepository(application)

    fun getLoadStatus(): MutableLiveData<Boolean> {
        return loadStatus
    }

    fun getAllMusicData(): MutableLiveData<MutableList<Music_Data_Entity>> {
        return musicRepository.getAllMusicData()
    }

    //如果使用Room的Order by ASC和DESC 不能用參數的方法 連column都不能用參數的 只能改用RawQuery
    fun getAllMusicDataOrderBy(sort_mode: Boolean, sort_type: Int): MutableList<Music_Data_Entity> {
        var orderBy = if (sort_mode) "DESC" else "ASC"
        var columnInfo: String = Setting_Entity.getSortType(sort_type)
        var simpleSQLiteQuery = SimpleSQLiteQuery("select * from ${Music_Data_Entity.TABLE_NAME} order by $columnInfo $orderBy")
        return musicRepository.getAllMusicDataOrderBy(simpleSQLiteQuery)
    }

    fun find_FileByName(fileName: String): Music_Data_Entity {
        return musicRepository.find_FileByName(fileName)
    }

    fun findAllName(): MutableList<String> {
        return musicRepository.findAllName()
    }

    fun findListData(path: String): Music_Data_Entity {
        return musicRepository.findListData(path)
    }

    fun insert(musicEntity: Music_Data_Entity) {
        musicRepository.insert(musicEntity)
    }

    fun update(musicEntity: Music_Data_Entity) {
        musicRepository.update(musicEntity)
    }

    fun delete(musicEntity: Music_Data_Entity) {
        musicRepository.delete(musicEntity)
    }

    fun deleteALL(musicEntity: MutableList<Music_Data_Entity>) {
        musicRepository.deleteALL(musicEntity)
    }

    //Setting

    fun getSettingNow(): Setting_Entity {
        return settingRepository.getSettingNow()
    }

    fun getSettingLiveData(): LiveData<Setting_Entity> {
        return settingRepository.getSettingLiveData()
    }

    fun insertSetting(setting_Entity: Setting_Entity) {
        settingRepository.insertSetting(setting_Entity)
    }

    fun updateSetting(setting_Entity: Setting_Entity) {
        settingRepository.updateSetting(setting_Entity)
    }


    //PlayListName
    fun getPlayListNameLiveData(): MutableLiveData<Boolean> {
        return playListRepository.getPlayListNameLiveData()
    }

    fun getAllListName(): MutableList<Music_ListName_Entity> {
        return playListRepository.getAllListName()
    }

}