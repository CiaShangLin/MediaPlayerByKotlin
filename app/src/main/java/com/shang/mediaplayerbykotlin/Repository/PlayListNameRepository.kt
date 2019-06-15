package com.shang.mediaplayerbykotlin.Repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_ListName_Entity

class PlayListNameRepository(context: Context) {
    private var mPlayListNameLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val playListNameDao by lazy {
        MusicDatabase.getMusicDatabase(context).getMusic_ListName_Dao()
    }

    fun getPlayListNameLiveData(): MutableLiveData<Boolean> {
        return mPlayListNameLiveData
    }

    fun getAllListName(): MutableList<Music_ListName_Entity> {
        return playListNameDao.getAll()
    }


}