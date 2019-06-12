package com.shang.mediaplayerbykotlin

import android.content.Intent
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_Interface

interface MyBroadcastReceiverUI {

    fun start(intent:Intent){}

    fun pause(){}

    fun next(){}

    fun insert(){}

    fun previous(){}

    fun release(){}

    fun reStart(){}

    fun looping(intent:Intent){}

    fun current_time(intent:Intent){}

    fun mode(intent:Intent){}

    fun reStore(){}
}