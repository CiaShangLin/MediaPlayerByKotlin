package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import com.shang.mediaplayerbykotlin.PlayMusicActivity
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.io.File
import java.util.*

/**
 * Created by Shang on 2018/8/14.
 */
open class MPC {

    companion object {
        val TAG = "MPC"

        var mediaPlayer: MediaPlayer ?= null
        var currentTime: Int = 0
        var index: Int = 0
        lateinit var mpc_mode: MPC_Interface
        lateinit var musicList: MutableList<Music_Data_Entity>
        var timer: Timer? = null
        var timerTask: TimerTask? = null

        fun startTimer(context:Context) {
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

}