package com.shang.mediaplayerbykotlin

import android.content.Context
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat

/**
 * Created by Shang on 2018/8/12.
 */
class FileUnits {

    companion object {

        var musicList = mutableListOf<File>()

        //最後修改時間轉成時間格式
        fun lastModifiedToSimpleDateFormat(time: Long): String {
            return SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒").format(time)
        }

        //取得所有Music底下的音樂檔
        fun findAllMusic(path: File) {
            try {
                var f = File(path.path)

                f.walkTopDown().filter {
                    it.isDirectory && (it.name in listOf<String>("Music", "music", "音樂"))
                }.forEach {
                    it.walk().filter {
                        it.isFile and (it.extension in listOf<String>("mp3", "flac", "3gp", "wav"))
                    }.forEach {
                        musicList.add(it)
                    }
                }
                findAllMusic(File(path.parent))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun findAllMusicFromContentResolver(context:Context):MutableList<Music_Data_Entity>{
            var entity= mutableListOf<Music_Data_Entity>()
            var uri=context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null)
            uri.moveToFirst()
            while(uri.moveToNext()){
                var name=uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                var duration=uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.DURATION))
                var path=uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.DATA))
                var modified=uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED))
                entity.add(Music_Data_Entity().apply {
                    this.name=name
                    this.path=path
                    this.duration=duration.toLong()
                    this.modified=modified.toLong()
                    this.favorite=false
                })
            }
            return entity
        }
    }


}