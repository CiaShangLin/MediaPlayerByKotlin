package com.shang.mediaplayerbykotlin

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.AsyncTask
import android.os.Message
import android.util.Log
import com.shang.mediaplayerbykotlin.Adapter.MusicDataAdapter
import com.shang.mediaplayerbykotlin.Room.MusicDatabase

import com.shang.mediaplayerbykotlin.Room.Music_Data_Dao
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity

class CheckFileRoom(var context: Context) : AsyncTask<Void, Void, Boolean>() {

    val TAG = "CheckFileRoom"
    lateinit var database: MusicDatabase
    lateinit var music_data_dao: Music_Data_Dao
    var beforeMap: MutableMap<String,Music_Data_Entity> = mutableMapOf()
    lateinit var musicList: MutableList<Music_Data_Entity>


    var start: Long = 0

    override fun onPreExecute() {
        super.onPreExecute()
        start = System.currentTimeMillis()

    }

    override fun doInBackground(vararg params: Void?): Boolean {

        //資料庫
        database = MusicDatabase.getMusicDatabase(context)
        music_data_dao = database.getMusic_Data_Dao()
        //轉成MAP
        music_data_dao.getAll().forEach {
            beforeMap.put(it.path,it)
        }

        musicList = FileUnits.findAllMusicFromContentResolver(context)
        Log.d(TAG, "size:" + musicList.size)

        ///storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1532109648631
        //picture:/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1532109648631
        musicList.forEach {
            if (music_data_dao.find_FileByName(it.name) == null) {
                try {
                    //it.picture=FileUnits.getPicture(it.picture,context)  //這樣是為了不要每次去提取資料 就先取得圖片 太花時間了
                    music_data_dao.insert(it)
                } catch (e: SQLiteConstraintException) {
                    Log.d(TAG, "已有這首:" + it.name)
                }
            }else{
                beforeMap.remove(it.path)
            }
        }


        for(it in beforeMap.iterator()){
            database.getMusic_Data_Dao().delete(Music_Data_Entity().apply {
                this.path=it.key
            })
            Log.d(TAG,"刪除:"+it.key)
        }


        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        Log.d(TAG, "finish:" + (System.currentTimeMillis() - start) / 1000.0)

        (context as MainActivity).handler.sendMessage(Message().apply {
            this.what = MusicDataAdapter.DATABASE_SUCCCESS
            this.obj = musicList
        })

        //insert不管有沒有 6秒
        //先query 5秒
        //不做取圖片1.186秒  取徒占了大份的時間
    }

}