package com.shang.mediaplayerbykotlin.MP

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.util.*

/**
 * Created by Shang on 2018/8/14.
 */
open class MPC :ViewModel(){

    public fun getLiveData():MutableLiveData<MutableList<Music_Data_Entity>>{
        return mutableData
    }

    companion object {
        val TAG = "MPC"

        var mediaPlayer: MediaPlayer? = null
        var currentTime: Int = 0
        var index: Int = -1
        lateinit var mpc_mode: MPC_Interface
        lateinit var musicList: MutableList<Music_Data_Entity>
        var timer: Timer? = null
        var timerTask: TimerTask? = null

        var mutableData=MutableLiveData<MutableList<Music_Data_Entity>>()

        // 1=修改日期 2=名稱長度 3=時間長度
        fun sort(mode: Boolean, type: Int) {
            var s=System.currentTimeMillis()
            if (mode) {   //升序
                when (type) {
                    1 -> {
                        musicList.sortByDescending { it.modified }
                    }
                    2 -> {
                        musicList.sortByDescending { it.name.length }
                    }
                    3 -> {
                        musicList.sortByDescending { it.duration }
                    }
                }
            } else {      //降序
                when (type) {
                    1 -> {
                        musicList.sortedBy { it.modified }
                    }
                    2 -> {
                        musicList.sortBy { it.name.length }
                    }
                    3 -> {
                        musicList.sortBy { it.duration }
                    }
                }
            }
            Log.d(TAG,((System.currentTimeMillis()-s)/1000.0).toString())
        }

        fun startTimer(context: Context) {
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