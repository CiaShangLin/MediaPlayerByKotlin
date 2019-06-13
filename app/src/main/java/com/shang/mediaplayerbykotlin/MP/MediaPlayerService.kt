package com.shang.mediaplayerbykotlin.MP

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.MyBroadcastReceiver
import com.shang.mediaplayerbykotlin.NotificationUnits


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


        startForeground(NotificationUnits.Notification_ID,
                NotificationUnits.instance(baseContext).notificationBuilder(this, "", "").build())
        Log.d(TAG, "startForeground")


        MPC.mpc_mode = MPC_normal(baseContext)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand:" + startId)
        when (intent?.action) {
            MyBroadcastReceiver.PLAY -> {
                MPC.mpc_mode.play()
            }
            MyBroadcastReceiver.START -> {
                MPC.mpc_mode.start()
                NotificationUnits.instance(this).update(this,
                        MPC.musicList.get(MPC.index).name,
                        MPC.musicList.get(MPC.index).picture)
                Log.d("TAG",MPC.musicList.get(MPC.index).name+" ")
            }
            MyBroadcastReceiver.PAUSE -> {
                MPC.mpc_mode.pause()
                NotificationUnits.instance(this)
                        .update(this,
                                MPC.musicList.get(MPC.index).name,
                                MPC.musicList.get(MPC.index).picture)

            }
            MyBroadcastReceiver.RESTART -> {
                MPC.mpc_mode.reStart()
                NotificationUnits.instance(this).update(this,
                        MPC.musicList.get(MPC.index).name,
                        MPC.musicList.get(MPC.index).picture)
            }
            MyBroadcastReceiver.NEXT -> {
                MPC.mpc_mode.next()
            }
            MyBroadcastReceiver.PREVIOUS -> {
                MPC.mpc_mode.previous()
            }

            MyBroadcastReceiver.MODE -> {

                var status: Boolean
                if (MPC.mpc_mode is MPC_normal) {
                    MPC.mpc_mode = MPC_random(baseContext)
                    status = true
                } else {
                    MPC.mpc_mode = MPC_normal(baseContext)
                    status = false
                }
                Log.d(TAG, MPC.mpc_mode.getName())

                LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                    this.action = MyBroadcastReceiver.MODE
                    this.putExtra(MyBroadcastReceiver.MODE, status)
                })
            }

            MyBroadcastReceiver.REPEAT -> {
                MPC.mpc_mode.setLooping()
            }

            MyBroadcastReceiver.INSERT -> {
                MPC.mpc_mode.insert()
            }

            MyBroadcastReceiver.SEEKBAR_MOVE -> {
                MPC.mpc_mode.seekbar_move(intent.getIntExtra(MyBroadcastReceiver.SEEKBAR_MOVE, 0))
            }

            MyBroadcastReceiver.RESTORE -> {
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
        MPC.mpc_mode.release()
        stopSelf()
        stopForeground(true)
    }
}