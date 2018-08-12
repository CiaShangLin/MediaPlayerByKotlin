package com.shang.mediaplayerbykotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    var MyLocal_Music_path: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()
    var MySDcard_Music_path: String = "/storage/9016-4EF8/"

    var file = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var publicPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        var privatePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()


        var bPlay = false
        button.setOnClickListener {
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

        var local_music = File(MyLocal_Music_path)
        var sdcard_music = File(MySDcard_Music_path)

        local_music.walk().filter {
            it.isFile and (it.extension=="mp3")
        }.forEach {
            Log.d("Music",it.name)
        }

        Log.d("Music","---------------------------------------")
        sdcard_music.walk().filter {
            it.isFile and (it.extension=="mp3")
        }.forEach {
            Log.d("Music",it.name)
        }

    }


}


