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

    companion object {
        val TAG="MediaPlayerService"
    }

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand:"+startId)

        when(intent!!.action){
            "START" -> MediaPlayerController.startMediaPlayer(intent.getStringExtra("path"))
            "STOP" -> MediaPlayerController.stopMediaPlayer()
            "RESTART" -> MediaPlayerController.reStartMediaPlayer()
            "NEXT"->""
            "PREVIOUS"->""
            "RESET"->""
        }

        return START_NOT_STICKY

        //START_STICKY : Service被殺掉, 系統會重啟, 但是Intent會是null。
        //START_NOT_STICKY : Service被系統殺掉, 不會重啟。
        //START_REDELIVER_INTENT : Service被系統殺掉, 重啟且Intent會重傳。

    }




    override fun onDestroy() {

        super.onDestroy()
        Log.d(TAG,"onDestroy")

        MediaPlayerController.releaseMediaPlayer()
        stopSelf()

    }
}