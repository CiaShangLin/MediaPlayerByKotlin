package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.media.MediaPlayer
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.io.File

/**
 * Created by Shang on 2018/8/14.
 */
open class MPC {

    companion object {
        val TAG = "MPC"

        var mediaPlayer: MediaPlayer ?= null
        var currentTime: Int = 0
        var index: Int = 0
        lateinit var mpc_mode: MPC_Interface
        lateinit var musicList: MutableList<Music_Data_Entity>

    }

}