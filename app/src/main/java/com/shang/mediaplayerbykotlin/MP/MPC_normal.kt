package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.util.Log
import com.shang.mediaplayerbykotlin.Broadcast.MyBroadcastReceiver


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

