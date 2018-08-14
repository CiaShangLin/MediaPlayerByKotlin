package com.shang.mediaplayerbykotlin

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.annotation.RequiresApi
import android.util.Log
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
           /* mediaPlayer = MediaPlayer()
            mediaPlayer.reset()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(file.get(0).path)
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener {
                if (it != null) {
                    it.start()
                    var time = mediaPlayer.duration / 1000
                    var minute = time / 60
                    var second = time - minute * 60
                    Log.d("Music", file.get(0).name)
                    Log.d("Music", "$minute 分/$second 秒")
                }
            }

            mediaPlayer.apply {

            }*/

            startService(Intent(this,MediaPlayerService::class.java).apply {
                putExtra("path",file.get(0).path)

            })


        }


        button2.setOnClickListener {
            MediaPlayerService.stop()
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


