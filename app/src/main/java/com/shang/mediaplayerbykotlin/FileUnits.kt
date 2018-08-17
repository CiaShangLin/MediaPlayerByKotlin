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

    companion object {

        var musicList = mutableListOf<File>()

        //最後修改時間轉成時間格式
        fun lastModifiedToSimpleDateFormat(time: Long): String {
            return SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒").format(time)
        }


        //取得所有Music底下的音樂檔
        fun findAllMusic(path: File){
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
    }


}