package com.shang.mediaplayerbykotlin.Repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity

class MusicRepository(context: Context) {
    private var musicDataMutableLiveData=MutableLiveData<MutableList<Music_Data_Entity>>()

    private val musicDataDao by lazy {
        MusicDatabase.getMusicDatabase(context).getMusic_Data_Dao()
    }

    fun getAllMusicData(): MutableLiveData<MutableList<Music_Data_Entity>> {
        musicDataMutableLiveData.postValue(musicDataDao.getAllMusicData())
        return musicDataMutableLiveData
    }

    fun getAllMusicDataOrderBy(query:SupportSQLiteQuery): MutableList<Music_Data_Entity>{
        return musicDataDao.getAllMusicDataOrderBy(query)
    }

    fun find_FileByName(fileName: String):Music_Data_Entity{
        return musicDataDao.find_FileByName(fileName)
    }

    fun findAllName():MutableList<String>{
        return musicDataDao.findAllName()
    }

    fun findListData(path:String):Music_Data_Entity{
        return musicDataDao.findListData(path)
    }

    fun insert(musicEntity: Music_Data_Entity){
        musicDataDao.insert(musicEntity)
    }

    fun update(musicEntity: Music_Data_Entity){
        musicDataDao.update(musicEntity)
    }

    fun delete(musicEntity: Music_Data_Entity){
        musicDataDao.delete(musicEntity)
    }

    fun deleteALL(musicEntity: MutableList<Music_Data_Entity>){
        musicDataDao.deleteALL(musicEntity)
    }
}