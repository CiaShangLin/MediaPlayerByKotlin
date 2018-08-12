package com.shang.mediaplayerbykotlin

import android.os.Environment
import android.util.Log
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat

/**
 * Created by Shang on 2018/8/12.
 */
class FileUnits {
    val MyLocal_Music_path: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()
    val MySDcard_Music_path: String = "/storage/9016-4EF8/"

    val fileUnits:FileUnits ?= null
    var musicList: MutableList<File> = mutableListOf()


    fun get_Music_List(path: String) {
        var file_music = File(path)

        file_music.walk().filter {
            it.isFile and (it.extension == "mp3")
        }.forEach {
            musicList.add(it)
        }
        /*Log.d("Music", "Name:" + it.nameWithoutExtension)  //取得沒有附檔名的檔名
        Log.d("Music", "Path:" + it.canonicalPath)          //路徑
        var s = SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Date(it.lastModified()))

        Log.d("Music", "modi:" + s)  //修改時間*/
        //播放長度 好像要在MediaPlayer尋找 您可以使用MediaMetadataRetriever獲取歌曲的持續時間。將METADATA_KEY_DURATION與extractMetadata（）函數結合使用。
    }

    fun getmusicList(): MutableList<File> {

        if(musicList.size==0){
            Log.d("Music","getmusicList()")
            get_Music_List(MyLocal_Music_path)
            get_Music_List(MySDcard_Music_path)
        }
        Log.d("Music",musicList.size.toString())


        return musicList
    }


}