package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.media.MediaPlayer

/**
 * Created by Shang on 2018/8/19.
 */
class MPC_random(var context: Context): MPC_Interface {
    override fun seekbar_move(time:Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLooping() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName():String{
        return "MPC_random"
    }

    override fun start() {
        //MPC.mediaPlayer= MediaPlayer.create(MPC.context)

    }

    override fun stop() {

    }

    override fun reset() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun next() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun previous() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun release() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}