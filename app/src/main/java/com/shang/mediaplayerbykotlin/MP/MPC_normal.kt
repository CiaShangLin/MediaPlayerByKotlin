package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.MP.MPC.Companion.stopTimer
import com.shang.mediaplayerbykotlin.MyBroadcastReceiver
import com.shang.mediaplayerbykotlin.NotificationUnits


class MPC_normal(context: Context): MPC_operate(context) {


    override fun getName(): String {
        return "MPC_normal"
    }

    //mode不一樣
    override fun next() {
        Log.d(MPC.TAG, "next()")
        if (MPC.index < MPC.musicList.size - 1) {
            release()
            MPC.index++
            start()
        } else { //發出廣播 做UI顯示
            super.sendBroadcast(Intent().apply {
                this.action = MyBroadcastReceiver.NEXT
            })
        }
    }



}

