package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.shang.mediaplayerbykotlin.MP.MPC.Companion.stopTimer
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity


class MPC_normal(var context: Context) : MPC_Interface {


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
                MPC.startTimer(context)
            }
        }

        MPC.mediaPlayer!!.setOnCompletionListener {
            stopTimer()
            release()
            next()
        }

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
            MPC.startTimer(context)
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
        } else { //發出廣播 做UI顯示
            context.sendBroadcast(Intent().apply {
                this.action = PlayMusicActivity.NEXT
            })
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
        } else {   //發出廣播 做UI顯示
            context.sendBroadcast(Intent().apply {
                action = PlayMusicActivity.PREVIOUS
            })
        }
    }


    override fun seekbar_move(time: Int) {
        if (MPC.mediaPlayer != null) {
            MPC.currentTime = time
            MPC.mediaPlayer!!.seekTo(time)
        }

    }

    override fun setLooping() {
        //setOnCompletionListener 不會啟動
        if (MPC.mediaPlayer != null) {
            var status = if (MPC.mediaPlayer!!.isLooping) "單曲循環播放關閉" else "單曲循環播放開啟"

            MPC.mediaPlayer!!.isLooping = !MPC.mediaPlayer!!.isLooping

            context.sendBroadcast(Intent().apply {
                action= PlayMusicActivity.LOOPING
                putExtra(MPC_Interface.STATUS,status)
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

    override fun reStore(){
        context.sendBroadcast(Intent().apply {
            this.action=PlayMusicActivity.RESTORE
            this.putExtra(MPC_Interface.NAME,MPC.musicList.get(MPC.index).name)
            this.putExtra(MPC_Interface.CURRENT_TIME,MPC.currentTime)
            this.putExtra(MPC_Interface.DURATION,MPC.musicList.get(MPC.index).duration)
            this.putExtra(MPC_Interface.STATUS,MPC.mediaPlayer!!.isPlaying)
        })
    }


}

