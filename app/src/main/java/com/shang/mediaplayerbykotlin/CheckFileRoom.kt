package com.shang.mediaplayerbykotlin

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.os.Message
import android.util.Log
import com.shang.mediaplayerbykotlin.Room.MusicDatabase

import com.shang.mediaplayerbykotlin.Room.Music_Data_Dao
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.io.File
import java.sql.SQLClientInfoException

class CheckFileRoom(var context: Context) : AsyncTask<Void, Void, Boolean>() {

    val TAG = "CheckFileRoom"
    lateinit var database: MusicDatabase
    lateinit var music_data_dao: Music_Data_Dao
    var mediaMata: MediaMetadataRetriever = MediaMetadataRetriever()
    lateinit var musicList: MutableList<Music_Data_Entity>
    lateinit var dataList: MutableList<Music_Data_Entity>

    var start: Long = 0

    override fun onPreExecute() {
        super.onPreExecute()
        start = System.currentTimeMillis()

    }

    override fun doInBackground(vararg params: Void?): Boolean {

        //資料庫
        database = MusicDatabase.getMusicDatabase(context)
        music_data_dao = database.getMusic_Data_Dao()

        musicList = FileUnits.findAllMusicFromContentResolver(context)
        Log.d(TAG, "size:" + musicList.size)
        /*musicList.sortByDescending {
            it.modified
        }
        for (i in musicList.indices) {
            Log.d(TAG, "name:" + musicList.get(i).name)
            Log.d(TAG, "duration:" + musicList.get(i).duration)
            Log.d(TAG, "path:" + musicList.get(i).path)
            Log.d(TAG, "modified:" + musicList.get(i).modified)
            Log.d(TAG, "favorite:" + musicList.get(i).favorite)
        }*/

        musicList.forEach {
            if (music_data_dao.find_FileByName(it.name) == null) {
                try {
                    music_data_dao.insert(it)
                } catch (e: SQLiteConstraintException) {
                    Log.d(TAG, "已有這首:" + it.name)
                }
            }
        }
        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        Log.d(TAG, "finish:" + (System.currentTimeMillis() - start) / 1000.0)

        (context as MainActivity).handler.sendMessage(Message().apply {
            this.what = MusicAdapter.DATABASE_SUCCCESS
            this.obj = musicList
        })

        //insert不管有沒有 6秒
        //先query 5秒
        //不做取圖片1.186秒  取徒占了大份的時間
    }

}