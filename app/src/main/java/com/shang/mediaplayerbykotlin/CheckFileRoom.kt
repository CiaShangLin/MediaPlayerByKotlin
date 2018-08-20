package com.shang.mediaplayerbykotlin

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import com.shang.mediaplayerbykotlin.Room.MusicDatabase

import com.shang.mediaplayerbykotlin.Room.Music_Data_Dao
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.io.File

/**
 * Created by Shang on 2018/8/20.
 */
class CheckFileRoom(var context: Context) : AsyncTask<Void, Void, Boolean>() {

    val TAG="CheckFileRoom"
    lateinit var database: MusicDatabase
    lateinit var music_data_dao: Music_Data_Dao
    lateinit var musicList: MutableList<File>
    lateinit var dataList: MutableList<Music_Data_Entity>

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Void?): Boolean {

        //資料庫
        database = MusicDatabase.getMusicDatabase(context)
        music_data_dao=database.getMusic_Data_Dao()

        FileUnits.findAllMusic(File(Environment.getExternalStorageDirectory().toString()))    //取得所有音樂

        musicList = FileUnits.musicList
        for(i in musicList.indices){
            try{
                music_data_dao.insert(Music_Data_Entity().apply {
                    this.name=musicList.get(i).nameWithoutExtension
                    this.time=MediaPlayer.create(context, Uri.fromFile(musicList.get(i))).duration
                    this.path=musicList.get(i).path
                    this.favorite=false
                })
            }catch (e: SQLiteConstraintException){   //重複插入primaryKey的話 會噴出錯誤
                Log.d(TAG,"已有這首:"+musicList.get(i).name)
            }
        }

        dataList = music_data_dao.getAll()
        for(i in dataList.indices){
            var l=(dataList.get(i).name in musicList)

        }


        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
    }
}