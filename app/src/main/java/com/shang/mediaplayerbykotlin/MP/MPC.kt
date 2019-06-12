package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.util.*


//音樂控制器和狀態
class MPC{

    companion object {
        val TAG = "MPC"

        var mediaPlayer: MediaPlayer? = null  //播放器
        var currentTime: Int = 0             //目前撥放到的時間
        var index: Int = -1                 //第幾首
        lateinit var mpc_mode: MPC_Interface  //操作模式
        lateinit var musicList: MutableList<Music_Data_Entity>  //要撥放的清單
        var timer: Timer? = null                              //定時器
        var timerTask: TimerTask? = null                       //定時任務

        // 1=修改日期 2=名稱長度 3=時間長度
        /*fun sort(mode: Boolean, type: Int) {
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
        }*/

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
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
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