package com.shang.mediaplayerbykotlin

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    var MyLocal_Music_path: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()
    var MySDcard_Music_path: String = "/storage/9016-4EF8/"

    var musicList:MutableList<File> = mutableListOf()

    var file = Environment.getExternalStorageDirectory()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bPlay = false
        button.setOnClickListener {
            Log.d("Music",musicList.size.toString())
            musicList.forEach {
                Log.d("Music","Name:"+it.nameWithoutExtension)  //取得沒有附檔名的檔名
                Log.d("Music","Path:"+it.canonicalPath)          //路徑
                var s=SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Date(it.lastModified()))

                Log.d("Music","modi:"+s)  //修改時間
                //播放長度 好像要在MediaPlayer尋找 您可以使用MediaMetadataRetriever獲取歌曲的持續時間。將METADATA_KEY_DURATION與extractMetadata（）函數結合使用。


            }

        }

        Find_Music(MyLocal_Music_path)
        Find_Music(MySDcard_Music_path)




    }

    fun Find_All_Directory_Parent(){
        while (true) {
            Log.d("TAG", "path:" + file.path)
            if (file.listFiles() != null) {
                for (item in file.listFiles()) {
                    var f: String
                    if (item.isFile) {
                        f = "File"
                    } else {
                        f = "Directory"
                    }
                    Log.d("TAG", "${item.name} :is $f")
                }

            }
            Log.d("TAG", "----------------------------------------")

            try {
                file = File(file.parent.toString())
            } catch (e: Exception) {
                break;
            }

        }
    }

    fun Find_Music(path:String){
        var file_music=File(path)

        file_music.walk().filter {
            it.isFile and (it.extension=="mp3")
        }.forEach {
            musicList.add(it)
        }
    }



}


