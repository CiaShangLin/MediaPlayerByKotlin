package com.shang.mediaplayerbykotlin

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.NonCancellable.start
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.ArrayList


class MainActivity : AppCompatActivity() {


    val TAG = "Music"
    lateinit var mediaPlayer: MediaPlayer

    var handler=object :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            Log.d("Music",msg?.what.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var file: MutableList<File> = FileUnits().getmusicList()

        button.setOnClickListener {


            startService(Intent(this,MediaPlayerService::class.java).apply {
                action="START"
                putExtra("path",file.get(0).path)
            })


        }


        button2.setOnClickListener {
            startService(Intent(this,MediaPlayerService::class.java).apply {
                action="STOP"
            })
        }

        button3.setOnClickListener {
            startService(Intent(this,MediaPlayerService::class.java).apply {
                action="RESTART"
            })
        }

    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {   //案上一頁會啟動
        super.onDestroy()

        Log.d(TAG, "onDestroy()")
    }


}


