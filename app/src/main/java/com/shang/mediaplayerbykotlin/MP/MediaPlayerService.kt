package com.shang.mediaplayerbykotlin.MP


import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.NotificationUnits
import com.shang.mediaplayerbykotlin.NotificationUnits.Companion.Notification_ID


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NotificationUnits.Notification_ID,
                    NotificationUnits.instance(baseContext).notificationBuilder(baseContext, "", "").build())
            Log.d(TAG, "startForeground")
        }

        MPC.mpc_mode = MPC_normal(baseContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand:" + startId)


        when (intent!!.action) {
            PlayMusicActivity.PLAY -> {
                MPC.mpc_mode.play()
            }
            PlayMusicActivity.START -> {
                MPC.mpc_mode.start()
            }
            PlayMusicActivity.PAUSE -> {
                MPC.mpc_mode.pause()
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

            PlayMusicActivity.MODE -> {

                var status: Boolean
                if (MPC.mpc_mode is MPC_normal) {
                    MPC.mpc_mode = MPC_random(baseContext)
                    status = true
                } else {
                    MPC.mpc_mode = MPC_normal(baseContext)
                    status = false
                }
                Log.d(TAG, MPC.mpc_mode.getName())

                sendBroadcast(Intent().apply {
                    this.action = PlayMusicActivity.MODE
                    this.putExtra(PlayMusicActivity.MODE, status)
                })
            }

            PlayMusicActivity.REPEAT -> {
                MPC.mpc_mode.setLooping()
            }

            PlayMusicActivity.INSERT -> {
                MPC.mpc_mode.insert()
            }

            PlayMusicActivity.SEEKBAR_MOVE -> {
                MPC.mpc_mode.seekbar_move(intent.getIntExtra(PlayMusicActivity.SEEKBAR_MOVE, 0))
            }

            PlayMusicActivity.RESTORE -> {
                MPC.mpc_mode.reStore()
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
        stopForeground(true)
    }
}