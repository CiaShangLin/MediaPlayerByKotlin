package com.shang.mediaplayerbykotlin

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
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

        /*FileUnits.findAllMusic(File(Environment.getExternalStorageDirectory().toString()))    //取得所有音樂

        musicList = FileUnits.musicList
        Log.d(TAG, "size:" + musicList.size.toString())



        for (i in musicList.indices) {
            if (music_data_dao.find_FileByName(musicList.get(i).nameWithoutExtension) == null) {
                try {
                    music_data_dao.insert(getFileToMusicDataEntity(musicList.get(i)))
                } catch (e: SQLiteConstraintException) {
                    Log.d(TAG, "已有這首:" + musicList.get(i).name)
                }
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
        }*/

        musicList = FileUnits.findAllMusicFromContentResolver(context)
        Log.d(TAG, "size:" + musicList.size)
        musicList.sortByDescending {
            it.modified
        }
        for (i in musicList.indices) {
            Log.d(TAG, "name:" +musicList.get(i).name)
            Log.d(TAG, "duration:" + musicList.get(i).duration)
            Log.d(TAG, "path:" + musicList.get(i).path)
            Log.d(TAG, "modified:" + musicList.get(i).modified)
            Log.d(TAG, "favorite:" + musicList.get(i).favorite)
        }

        musicList.forEach{
            try{
                music_data_dao.insert(it)
            }catch (e:SQLiteConstraintException){
                Log.d(TAG, "已有這首:" + it.name)
            }
        }





        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        Log.d(TAG, "finish:" + (System.currentTimeMillis() - start) / 1000)

        //27 秒 有create
        //沒create 4秒
    }

    /*fun getFileToMusicDataEntity(file:File): Music_Data_Entity {
        mediaMata.setDataSource(file.path)
        var entity=Music_Data_Entity().apply {
            this.name=file.nameWithoutExtension
            this.favorite=false
            this.path=file.path
            this.time=mediaMata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
        }
        return entity
    }*/
}