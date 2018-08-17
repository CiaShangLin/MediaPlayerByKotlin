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

    companion object {
        //最後修改時間轉成時間格式
        fun lastModifiedToSimpleDateFormat(time:Long):String{
            return SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒").format(time)
        }
    }

    fun getMusicList(): MutableList<File> {
        var musicList: MutableList<File> = mutableListOf()

        if(musicList.size==0){
            musicList.addAll(get_Music_List(MyLocal_Music_path))
            musicList.addAll(get_Music_List(MySDcard_Music_path))
        }
        Log.d("Music",musicList.size.toString())

        return musicList
    }


    //還要修改 要改成找到Music所有的MP3
    private fun get_Music_List(path: String):MutableList<File> {
        var musicList: MutableList<File> = mutableListOf()
        var file_music = File(path)

        file_music.walkTopDown().filter {
            it.isFile and (it.extension in listOf<String>("mp3","flac","3gp","wav"))
        }.forEach {
            musicList.add(it)
        }

        return musicList
        /*Log.d("Music", "Name:" + it.nameWithoutExtension)  //取得沒有附檔名的檔名
        Log.d("Music", "Path:" + it.canonicalPath)          //路徑
        var s = SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Date(it.lastModified()))

        Log.d("Music", "modi:" + s)  //修改時間*/
        //播放長度 好像要在MediaPlayer尋找 您可以使用MediaMetadataRetriever獲取歌曲的持續時間。將METADATA_KEY_DURATION與extractMetadata（）函數結合使用。
    }

}