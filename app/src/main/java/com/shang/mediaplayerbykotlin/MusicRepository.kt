package com.shang.mediaplayerbykotlin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity

class MusicRepository(context: Context) {

    private val musicDataDao by lazy {
        MusicDatabase.getMusicDatabase(context).getMusic_Data_Dao()
    }

    fun getAllMusicData(): LiveData<MutableList<Music_Data_Entity>> {
        return musicDataDao.getAll()
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