package com.shang.mediaplayerbykotlin.MP

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log


/**
 * Created by Shang on 2018/8/12.
 */
class MediaPlayerService : Service() {

    companion object {
        val TAG = "MediaPlayerService"
    }

   // lateinit var mpc_mode : MPC_Interface

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        //mpc_mode=MPC.mpc_mode
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand:" + startId)

        when (intent!!.action) {
            "START" -> MPC.mpc_mode.start(intent.getStringExtra("path"))
            "STOP" -> MPC.mpc_mode.stop()
            "RESTART" -> { }
            "NEXT" -> ""
            "PREVIOUS" -> ""
            "RESET" -> { }
            "MODE" ->{ Log.d(TAG, MPC.mpc_mode.getName())
                         MPC.mpc_mode= MPC_random()
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