package com.shang.mediaplayerbykotlin.MP

import android.media.MediaPlayer
import java.io.File

/**
 * Created by Shang on 2018/8/14.
 */
open class MPC {

    var v:Int=0
    object mpc

    companion object {
        val TAG = "MPC"


        lateinit var mediaPlayer: MediaPlayer

        var currentTime: Int = 0
        var index: Int = 0
        var mpc_mode: MPC_Interface = MPC_normal()

        lateinit var musicList:MutableList<File>

    }
}