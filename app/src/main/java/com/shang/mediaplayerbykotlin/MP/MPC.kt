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

    companion object {
        val TAG = "MPC"


        lateinit var mediaPlayer: MediaPlayer
        var currentTime: Int = 0
        var index: Int = 0
        lateinit var mpc_mode: MPC_Interface
        lateinit var musicList: MutableList<Music_Data_Entity>

    }
}