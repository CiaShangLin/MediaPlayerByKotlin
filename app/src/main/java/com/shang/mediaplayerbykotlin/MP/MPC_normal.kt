package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.HandlerThread
import android.util.Log
import com.shang.mediaplayerbykotlin.PlayMusicActivity
import org.jetbrains.anko.toast
import java.util.*
import java.util.logging.Handler
import java.util.logging.LogRecord


class MPC_normal(var context: Context) : MPC_Interface {


    var timer: Timer? = null
    var timerTask: TimerTask? = null


    override fun getName(): String {
        return "MPC_normal"
    }

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
        MPC.mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC);
        MPC.mediaPlayer!!.setDataSource(MPC.musicList.get(MPC.index).path)
        MPC.mediaPlayer!!.prepare()
        MPC.mediaPlayer!!.setOnPreparedListener {
            if (it != null) {
                it.start()
                context.sendBroadcast(Intent().apply {
                    this.action = PlayMusicActivity.START
                    this.putExtra(MPC_Interface.NAME, MPC.musicList.get(MPC.index).name)
                    this.putExtra(MPC_Interface.DURATION, MPC!!.mediaPlayer!!.duration)
                })
                startTimer()
            }
        }

        MPC.mediaPlayer!!.setOnCompletionListener {
            stopTimer()
            release()
            next()
        }

        context.sendBroadcast(Intent().apply {
            action = PlayMusicActivity.START
        })

    }


    override fun pause() {
        Log.d(MPC.TAG, "pause()")
        if (MPC.mediaPlayer != null && MPC!!.mediaPlayer!!.isPlaying) {
            MPC.currentTime = MPC!!.mediaPlayer!!.currentPosition
            MPC!!.mediaPlayer!!.pause()
        }


        stopTimer()

        context.sendBroadcast(Intent().apply {
            action = PlayMusicActivity.PAUSE
        })
    }

    override fun reStart() {
        Log.d(MPC.TAG, "reStart()")
        if (MPC.mediaPlayer != null) {
            MPC.mediaPlayer!!.seekTo(MPC.currentTime)
            MPC.mediaPlayer!!.start()
            startTimer()
            context.sendBroadcast(Intent().apply {
                action = PlayMusicActivity.RESTART
            })
        }

    }


    override fun next() {
        Log.d(MPC.TAG, "next()")
        if (MPC.index < MPC.musicList.size - 1) {
            release()
            MPC.index++
            start()
        } else {
            context.toast("已經是最後一首了")
        }
    }


    override fun insert() {   //插播
        release()
        start()
    }

    override fun previous() {
        if (MPC.index > 0) {
            release()
            MPC.index--
            start()
        } else {
            context.toast("這是第一首")
        }
    }


    override fun seekbar_move(time: Int) {

    }

    override fun setLooping() {
        if (MPC.mediaPlayer != null)
            MPC.mediaPlayer!!.isLooping = !MPC.mediaPlayer!!.isLooping
    }

    override fun release() {
        Log.d(MPC.TAG, "release()")
        if (MPC.mediaPlayer != null) {
            MPC.currentTime = 0
            MPC.mediaPlayer!!.release()
            MPC.mediaPlayer = null
        }
        stopTimer()
    }

    fun startTimer() {
        Log.d(MPC.TAG, "startTimer()")
        if (timer == null) {
            timer = Timer(true)
            timerTask = object : TimerTask() {
                override fun run() {
                    var intent = Intent().apply {
                        this.action = PlayMusicActivity.CURRENT_TIME
                        this.putExtra(MPC_Interface.CURRENT_TIME, MPC.mediaPlayer!!.currentPosition)
                    }
                    context.sendBroadcast(intent)
                }
            }
            timer?.scheduleAtFixedRate(timerTask, 0, 1000)
        }
    }

    fun stopTimer() {
        Log.d(MPC.TAG, "stopTimer()")
        timer?.cancel()
        timer = null
        timerTask = null
    }


}