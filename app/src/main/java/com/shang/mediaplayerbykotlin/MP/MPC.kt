package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.media.MediaPlayer
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.io.File

/**
 * Created by Shang on 2018/8/14.
 */
open class MPC {

    var v: Int = 0

    object mpc

    companion object {
        val TAG = "MPC"


        lateinit var mediaPlayer: MediaPlayer

        lateinit var context: Context
        var currentTime: Int = 0
        var index: Int = 0
        var mpc_mode: MPC_Interface = MPC_normal()

        lateinit var musicList: MutableList<Music_Data_Entity>

    }
}