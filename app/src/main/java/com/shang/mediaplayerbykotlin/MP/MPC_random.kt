package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.util.Log
import com.shang.mediaplayerbykotlin.Broadcast.MyBroadcastReceiver

/**
 * Created by Shang on 2018/8/19.
 */
class MPC_random(context: Context) : MPC_operate(context) {


    private val indexMap: MutableMap<Int, Boolean> by lazy {
        mutableMapOf<Int, Boolean>()
    }

    override fun getName(): String {
        return "MPC_random"
    }


    //mode不一樣
    override fun next() {
        Log.d(MPC.TAG, "next():"+MPC.index)
        if (indexMap.size < MPC.musicList.size) {  //map還沒播完
            release()
            var index = (Math.random() * MPC.musicList.size).toInt()
            while (indexMap.get(index) == true) {   //true代表已經播過了
                index = (index + 1) % MPC.musicList.size
            }
            MPC.index = index
            indexMap.put(index, true)
            start()
        }else{
            super.sendBroadcast(Intent().apply {
                this.action = MyBroadcastReceiver.NEXT
            })
        }
    }

}