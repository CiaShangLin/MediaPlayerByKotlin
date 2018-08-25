package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.shang.mediaplayerbykotlin.R

/**
 * Created by Shang on 2018/8/19.
 */
class MPC_normal(var context: Context) : MPC_Interface {

    override fun getName():String{
        return "MPC_normal"
    }

    override fun reStart() {
        if(MPC.mediaPlayer !=null){
            MPC.mediaPlayer.seekTo(MPC.currentTime)
            MPC.mediaPlayer.start()
        }
        Log.d(MPC.TAG, "reStart()")
    }

    override fun stop() {
        if(MPC.mediaPlayer !=null && MPC.mediaPlayer.isPlaying){
            MPC.currentTime = MPC.mediaPlayer.currentPosition
            MPC.mediaPlayer.pause()
        }
        Log.d(MPC.TAG, "pause()")
    }

    override fun reset() {
        if(MPC.mediaPlayer !=null && MPC.mediaPlayer.isPlaying){
            MPC.mediaPlayer.reset()
        }
    }

    override fun next() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun previous() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun release() {
        if(MPC.mediaPlayer !=null)
            MPC.mediaPlayer.release()
    }

    override fun start(path: String) {
        MPC.mediaPlayer = MediaPlayer()
        MPC.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        MPC.mediaPlayer.setDataSource(path)
        MPC.mediaPlayer.prepare()
        MPC.mediaPlayer.setOnPreparedListener {
            if (it != null) {
                it.start()
                var time = MPC.mediaPlayer.duration / 1000
                var minute = time / 60
                var second = time - minute * 60
                Log.d(MPC.TAG, "$minute 分/$second 秒")
            }
        }

        MPC.mediaPlayer.setOnCompletionListener {
            MPC.mediaPlayer.release()
        }


        context.sendBroadcast(Intent().apply {
            this.action=context.getString(R.string.MyRecevier)
            this.putExtra(context.getString(R.string.MyRecevier),this.getStringExtra("path"))
        })

    }


}