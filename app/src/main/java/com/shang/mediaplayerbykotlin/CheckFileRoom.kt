package com.shang.mediaplayerbykotlin

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.shang.mediaplayerbykotlin.Activity.MainActivity

import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.ViewModel.MediaPlayerModel

class CheckFileRoom(var context: Context, var mediaPlayerModel: MediaPlayerModel) : AsyncTask<Void, Void, Boolean>() {

    val TAG = "CheckFileRoom"

    var beforeMap: MutableMap<String, Music_Data_Entity> = mutableMapOf()
    lateinit var musicList: MutableList<Music_Data_Entity>

    override fun doInBackground(vararg params: Void?): Boolean {

        //轉成MAP
        mediaPlayerModel.getAllMusicData().value?.forEach {
            beforeMap.put(it.path, it)
        }

        //取得手機所有音檔從ContentResolver
        musicList = FileUnits.findAllMusicFromContentResolver(context)
        Log.d(TAG, "size:" + musicList.size)


        if (beforeMap.size == 0) {
            Log.d(TAG, "第一次載入")
            musicList = FileUnits.getPicture(musicList, context)  //一次取得所有圖片
            musicList.forEach {
                if (mediaPlayerModel.find_FileByName(it.name) == null) {
                    mediaPlayerModel.insert(it)
                    Log.d(TAG, "新增這首:" + it.name)
                }
            }
        } else {
            Log.d(TAG, "檢查載入")
            musicList.forEach {
                if (mediaPlayerModel.find_FileByName(it.name) == null) {
                    //這樣是為了不要每次去提取資料 就先取得圖片 太花時間了
                    FileUnits.getOnePicture(it.picture, context)
                    mediaPlayerModel.insert(it)
                    Log.d(TAG, "新增這首:" + it.name)
                } else {
                    beforeMap.remove(it.path)
                }
            }
        }


        //手機端刪除 但是資料庫還在 所以要刪除
        for (it in beforeMap.iterator()) {
            mediaPlayerModel.delete(Music_Data_Entity().apply {
                this.path = it.key
            })
            Log.d(TAG, "刪除:" + it.key)
        }

        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        mediaPlayerModel.getLoadStatus().value=true

        //不做取圖片1.186秒  讀取占了大份的時間
    }


}