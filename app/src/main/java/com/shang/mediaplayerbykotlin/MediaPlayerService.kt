package com.shang.mediaplayerbykotlin

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Message
import android.util.Log
import java.security.Provider

/**
 * Created by Shang on 2018/8/12.
 */
class MediaPlayerService :Service() {


    val TAG="MediaPlayerService"

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand")
        mediaPlayer = MediaPlayer()
        mediaPlayer.reset()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(intent!!.getStringExtra("path"))
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener {
            if (it != null) {
                it.start()
            }
        }
        return START_NOT_STICKY

    }


    companion object {
        lateinit var mediaPlayer:MediaPlayer
        fun stop(){
            mediaPlayer.stop()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
        mediaPlayer.release()

    }
}