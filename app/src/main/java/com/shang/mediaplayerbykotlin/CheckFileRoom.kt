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
        Log.d(TAG,"size:"+musicList.size.toString())

        for(i in musicList.indices){   //直接寫入 不管已有無
            Log.d(TAG,i.toString())
            try{
                music_data_dao.insert(Music_Data_Entity().apply {
                    this.name=musicList.get(i).nameWithoutExtension
                    this.time=MediaPlayer.create(context, Uri.fromFile(musicList.get(i))).duration
                    this.path=musicList.get(i).path
                    this.favorite=false
                    Log.d(TAG,name+" "+time+" "+path+" "+favorite)
                })

            }catch (e: SQLiteConstraintException){   //重複插入primaryKey的話 會噴出錯誤
                Log.d(TAG,"已有這首:"+musicList.get(i).name)
            }
        }


        //之後再轉list to map的方法
        var map:MutableMap<String,File> = mutableMapOf()
        musicList.forEach {
            map.put(it.nameWithoutExtension,it)
        }

        dataList = music_data_dao.getAll()
        for(i in dataList.indices){
            var d=dataList.get(i)
            var m=map.get(d.name)
            if(m!=null){    //兩邊都有 但有可能改路徑
                if(m.path != d.path){
                    music_data_dao.update(Music_Data_Entity().apply {
                        this.name=d.name
                        this.time=d.time        //不給予值得話 他會變成預設值
                        this.path=m.path         //唯一修改路徑
                        this.favorite=d.favorite
                    })
                }
            }else{   //資料庫有 但是現在沒有 所以刪除
                music_data_dao.delete(Music_Data_Entity().apply {
                    this.name=d.name
                })
            }
        }


        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        Log.d(TAG,"fanlsh")



    }
}