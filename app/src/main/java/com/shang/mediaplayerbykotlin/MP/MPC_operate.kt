package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.shang.mediaplayerbykotlin.Broadcast.MyBroadcastReceiver
import com.shang.mediaplayerbykotlin.NotificationUnits

open abstract class MPC_operate(var context: Context) : MPC_Interface {

    private val mLocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(context)
    }

    override abstract fun getName(): String
    override abstract fun next()

    override fun play() {
        if (MPC.mediaPlayer == null) {
            start()
        } else if (MPC.mediaPlayer!!.isPlaying) {
            pause()
        } else {
            reStart()
        }
    }

    override fun start() {
        Log.d(MPC.TAG, "start()")

        MPC.mediaPlayer = MediaPlayer()
        MPC.mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC);
        MPC.mediaPlayer?.setDataSource(MPC.musicList.get(MPC.index).path)
        MPC.mediaPlayer?.prepare()
        MPC.mediaPlayer?.setOnPreparedListener {
            if (it != null) {
                it.start()
                sendBroadcast(Intent().apply {
                    this.action = MyBroadcastReceiver.START
                    this.putExtra(MPC_Interface.NAME, MPC.musicList.get(MPC.index).name)
                    this.putExtra(MPC_Interface.DURATION, MPC?.mediaPlayer?.duration)
                })
                MPC.startTimer(context)
                notificationUpdate()
            }
        }

        MPC.mediaPlayer!!.setOnCompletionListener {
            var intent = Intent(context, MediaPlayerService::class.java).apply {
                this.action = MyBroadcastReceiver.NEXT
            }
            startService(intent)
        }
    }

    override fun pause() {
        Log.d(MPC.TAG, "pause()")
        if (MPC.mediaPlayer != null && MPC?.mediaPlayer!!.isPlaying) {
            MPC.currentTime = MPC?.mediaPlayer!!.currentPosition
            MPC!!.mediaPlayer!!.pause()
        }

        MPC.stopTimer()

        sendBroadcast(Intent().apply {
            action = MyBroadcastReceiver.PAUSE
        })

        notificationUpdate()
    }


    override fun reStart() {
        Log.d(MPC.TAG, "reStart()")
        if (MPC.mediaPlayer != null) {
            MPC.mediaPlayer?.seekTo(MPC.currentTime)
            MPC.mediaPlayer?.start()
            MPC.startTimer(context)

            sendBroadcast(Intent().apply {
                action = MyBroadcastReceiver.RESTART
            })
        }

        notificationUpdate()
    }



    override fun insert() {
        release()
        start()
    }

    override fun previous() {
        if (MPC.index > 0) {
            release()
            MPC.index--
            start()
        } else {   //發出廣播 做UI顯示

            sendBroadcast(Intent().apply {
                action = MyBroadcastReceiver.PREVIOUS
            })
        }
    }

    override fun seekbar_move(time: Int) {
        if (MPC.mediaPlayer != null) {
            MPC.currentTime = time
            MPC.mediaPlayer?.seekTo(time)
        }
    }

    override fun setLooping() {    //setOnCompletionListener 不會啟動

        if (MPC.mediaPlayer != null) {
            var status = if (MPC.mediaPlayer!!.isLooping) "單曲循環播放關閉" else "單曲循環播放開啟"

            MPC.mediaPlayer!!.isLooping = !MPC.mediaPlayer!!.isLooping

            sendBroadcast(Intent().apply {
                action = MyBroadcastReceiver.LOOPING
                putExtra(MPC_Interface.STATUS, status)
            })

        }
    }


    override fun release() {
        Log.d(MPC.TAG, "release()")
        if (MPC.mediaPlayer != null) {
            MPC.currentTime = 0
            MPC.mediaPlayer!!.release()
            MPC.mediaPlayer = null
        }
        MPC.stopTimer()
    }

    override fun reStore() {
        sendBroadcast(Intent().apply {
            this.action = MyBroadcastReceiver.RESTORE
            this.putExtra(MPC_Interface.NAME, MPC.musicList.get(MPC.index).name)
            this.putExtra(MPC_Interface.CURRENT_TIME, MPC.currentTime)
            this.putExtra(MPC_Interface.DURATION, MPC.musicList.get(MPC.index).duration)
            this.putExtra(MPC_Interface.STATUS, MPC.mediaPlayer!!.isPlaying)
        })
    }

    protected fun sendBroadcast(intent:Intent){
        mLocalBroadcastManager.sendBroadcast(intent)
    }

    protected fun startService(intent:Intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        }else{
            context.startService(intent)
        }
    }

    protected fun notificationUpdate(){
        NotificationUnits.instance(context).update(context,
                MPC.musicList[MPC.index].name,
                MPC.musicList[MPC.index].picture)
    }

}