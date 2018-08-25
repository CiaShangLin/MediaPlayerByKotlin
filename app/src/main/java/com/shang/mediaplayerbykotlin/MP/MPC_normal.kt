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

    override fun seekbar_move(time:Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLooping() {
        if (MPC.mediaPlayer != null)
            MPC.mediaPlayer!!.isLooping = !MPC.mediaPlayer!!.isLooping
    }

    override fun reStart() {
        if (MPC.mediaPlayer != null) {
            MPC.mediaPlayer!!.seekTo(MPC.currentTime)
            MPC.mediaPlayer!!.start()
        }
        Log.d(MPC.TAG, "reStart()")
    }

    override fun stop() {
        if (MPC.mediaPlayer != null && MPC!!.mediaPlayer!!.isPlaying) {
            MPC.currentTime = MPC!!.mediaPlayer!!.currentPosition
            MPC!!.mediaPlayer!!.pause()
        }
        Log.d(MPC.TAG, "pause()")
    }

    override fun reset() {
        if (MPC.mediaPlayer != null && MPC!!.mediaPlayer!!.isPlaying) {
            MPC.mediaPlayer!!.reset()
            MPC.currentTime = 0
        }
    }

    override fun next() {
        if (MPC.index < MPC.musicList.size - 1) {
            release()
            MPC.index++
            start()
        } else {
            context.toast("已經是最後一首了")
        }
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

    override fun release() {
        if (MPC.mediaPlayer != null) {
            MPC.currentTime = 0
            MPC.mediaPlayer!!.release()
            MPC.mediaPlayer = null
        }
    }

    override fun start() {
        Log.d("MPC", MPC.currentTime.toString() + " " + (MPC.mediaPlayer == null).toString())

        if (MPC.currentTime == 0 && MPC.mediaPlayer == null) {
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
        } else if (MPC.mediaPlayer!!.isPlaying) {
            stop()
        } else {
            reStart()
        }
    }

    fun startTimer() {
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
        timer?.cancel()
        timer = null
        timerTask = null
    }


}