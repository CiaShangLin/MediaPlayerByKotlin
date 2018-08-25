package com.shang.mediaplayerbykotlin.MP

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.HandlerThread
import android.util.Log
import com.shang.mediaplayerbykotlin.PlayMusicActivity
import org.jetbrains.anko.toast
import java.util.*
import java.util.logging.Handler
import java.util.logging.LogRecord


/**
 * Created by Shang on 2018/8/19.
 */
class MPC_normal(var context: Context) : MPC_Interface {


    var handler: android.os.Handler = android.os.Handler()
    var timer:Timer=Timer(true)
    var timerTask=object :TimerTask(){
        override fun run() {
            var intent=Intent().apply {
                this.action=PlayMusicActivity.CURRENT_TIME
                this.putExtra(MPC_Interface.CURRENT_TIME,MPC.mediaPlayer.currentPosition)
            }
            context.sendBroadcast(intent)
        }
    }

    override fun getName(): String {
        return "MPC_normal"
    }

    override fun reStart() {
        if (MPC.mediaPlayer != null) {
            MPC.mediaPlayer.seekTo(MPC.currentTime)
            MPC.mediaPlayer.start()
        }
        Log.d(MPC.TAG, "reStart()")
    }

    override fun stop() {
        if (MPC.mediaPlayer != null && MPC.mediaPlayer.isPlaying) {
            MPC.currentTime = MPC.mediaPlayer.currentPosition
            MPC.mediaPlayer.pause()
        }
        Log.d(MPC.TAG, "pause()")
    }

    override fun reset() {
        if (MPC.mediaPlayer != null && MPC.mediaPlayer.isPlaying) {
            MPC.mediaPlayer.reset()
        }
    }

    override fun next() {
        if(MPC.index<MPC.musicList.size-1){
            MPC.index++
            MPC.currentTime=0
            start(MPC.musicList.get(MPC.index).path)
        }else{
            context.toast("已經是最後一首了")
        }
    }

    override fun previous() {

    }

    override fun release() {
        if (MPC.mediaPlayer != null)
            MPC.mediaPlayer.release()
    }

    override fun start(path: String) {
        if(MPC.currentTime==0){
            MPC.mediaPlayer = MediaPlayer()
            MPC.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            MPC.mediaPlayer.setDataSource(path)
            MPC.mediaPlayer.prepare()
            MPC.mediaPlayer.setOnPreparedListener {
                if (it != null) {
                    it.start()
                    context.sendBroadcast(Intent().apply {
                        this.action = PlayMusicActivity.START

                        this.putExtra(MPC_Interface.NAME, MPC.musicList.get(MPC.index).name)
                        this.putExtra(MPC_Interface.DURATION, MPC.mediaPlayer.duration)
                    })

                    timer.schedule(timerTask,Date(),1000)
                }
            }

            MPC.mediaPlayer.setOnCompletionListener {
                MPC.mediaPlayer.release()
                timer.cancel()
                next()
            }
        }else if(MPC.mediaPlayer.isPlaying){
            stop()
        }else{
            reStart()
        }
    }




}