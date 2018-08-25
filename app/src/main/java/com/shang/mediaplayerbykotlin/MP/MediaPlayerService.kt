package com.shang.mediaplayerbykotlin.MP

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.shang.mediaplayerbykotlin.MainActivity
import com.shang.mediaplayerbykotlin.PlayMusicActivity
import com.shang.mediaplayerbykotlin.R


/**
 * Created by Shang on 2018/8/12.
 */
class MediaPlayerService : Service() {

    companion object {
        val TAG = "MediaPlayerService"
    }

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        MPC.mpc_mode = MPC_normal(baseContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand:" + startId)

        when (intent!!.action) {
            PlayMusicActivity.START -> {
                MPC.mpc_mode.start()
            }
            PlayMusicActivity.STOP -> {
                MPC.mpc_mode.stop()
            }
            PlayMusicActivity.RESTART -> {
                MPC.mpc_mode.reStart()
            }
            PlayMusicActivity.NEXT -> {
                MPC.mpc_mode.next()
            }
            PlayMusicActivity.PREVIOUS -> {
                MPC.mpc_mode.previous()
            }
            PlayMusicActivity.RESET -> {
                MPC.mpc_mode.reset()
            }
            PlayMusicActivity.MODE -> {
                Log.d(TAG, MPC.mpc_mode.getName())
                MPC.mpc_mode = MPC_random(baseContext)
            }
        }

        return START_NOT_STICKY

        //START_STICKY : Service被殺掉, 系統會重啟, 但是Intent會是null。
        //START_NOT_STICKY : Service被系統殺掉, 不會重啟。
        //START_REDELIVER_INTENT : Service被系統殺掉, 重啟且Intent會重傳。

    }

    override fun onDestroy() {

        super.onDestroy()
        Log.d(TAG, "onDestroy")

        stopSelf()
    }
}