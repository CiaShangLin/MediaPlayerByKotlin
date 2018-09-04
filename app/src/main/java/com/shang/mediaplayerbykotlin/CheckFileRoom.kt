package com.shang.mediaplayerbykotlin

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Message
import android.util.Log
import com.shang.mediaplayerbykotlin.Activity.MainActivity
import com.shang.mediaplayerbykotlin.Adapter.MusicDataAdapter
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.Room.MusicDatabase

import com.shang.mediaplayerbykotlin.Room.Music_Data_Dao
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.Setting_Entity

class CheckFileRoom(var context: Context) : AsyncTask<Void, Void, Boolean>() {

    val TAG = "CheckFileRoom"
    lateinit var database: MusicDatabase
    lateinit var music_data_dao: Music_Data_Dao
    var beforeMap: MutableMap<String, Music_Data_Entity> = mutableMapOf()
    lateinit var musicList: MutableList<Music_Data_Entity>
    lateinit var setting_entity: Setting_Entity

    var start: Long = 0

    override fun onPreExecute() {
        super.onPreExecute()
        start = System.currentTimeMillis()
    }

    override fun onProgressUpdate(vararg values: Void?) {
        super.onProgressUpdate(*values)
    }

    override fun doInBackground(vararg params: Void?): Boolean {

        //資料庫
        database = MusicDatabase.getMusicDatabase(context)
        music_data_dao = database.getMusic_Data_Dao()

        //轉成MAP
        music_data_dao.getAll().forEach {
            beforeMap.put(it.path, it)
        }

        //取得手機所有音檔
        musicList = FileUnits.findAllMusicFromContentResolver(context)
        Log.d(TAG, "size:" + musicList.size)


        if (beforeMap.size == 0) {
            Log.d(TAG, "第一次載入")
            musicList = FileUnits.getPicture(musicList, context)  //一次取得所有圖片
            musicList.forEach {
                if (music_data_dao.find_FileByName(it.name) == null) {
                    music_data_dao.insert(it)
                    Log.d(TAG, "新增這首:" + it.name)
                }
            }
        } else {
            Log.d(TAG, "檢查載入")
            musicList.forEach {
                if (music_data_dao.find_FileByName(it.name) == null) {
                    //這樣是為了不要每次去提取資料 就先取得圖片 太花時間了
                    FileUnits.getOnePicture(it.picture, context)
                    music_data_dao.insert(it)
                    Log.d(TAG, "新增這首:" + it.name)
                } else {
                    beforeMap.remove(it.path)
                }
            }
        }


        //手機端刪除 但是資料庫還在 所以要刪除
        for (it in beforeMap.iterator()) {
            database.getMusic_Data_Dao().delete(Music_Data_Entity().apply {
                this.path = it.key
            })
            Log.d(TAG, "刪除:" + it.key)
        }

        musicList = database.getMusic_Data_Dao().getAll()

        insertSetting()  //插入設定

        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        Log.d(TAG, "finish:" + (System.currentTimeMillis() - start) / 1000.0)
        Log.d(TAG, "setting:" + setting_entity.sort_mode+" "+setting_entity.sort_type)
        MPC.musicList = musicList
        MPC.sort(setting_entity.sort_mode,setting_entity.sort_type)

        context.sendBroadcast(Intent().apply {
            this.action=MainActivity.DATABASE_SUCCCESS
        })

        //不做取圖片1.186秒  取徒占了大份的時間
    }

    fun insertSetting() {
        database.getSetting_Dao().insertSetting(Setting_Entity().apply {
            this.name = Setting_Entity.key
        })

        setting_entity=database.getSetting_Dao().getSetting()
    }

}