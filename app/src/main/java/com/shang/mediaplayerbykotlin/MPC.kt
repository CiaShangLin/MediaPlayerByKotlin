package com.shang.mediaplayerbykotlin

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat

/**
 * Created by Shang on 2018/8/14.
 */
open class MPC {

    companion object {
        val TAG = "MPC"


        lateinit var mediaPlayer: MediaPlayer

        var currentTime: Int = 0
        var index: Int = 0
        var mpc_mode: MPC_Interface = MPC_normal()

        lateinit var musicList:MutableList<File>

    }
}