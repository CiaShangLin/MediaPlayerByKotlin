package com.shang.mediaplayerbykotlin

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import java.text.SimpleDateFormat

/**
 * Created by Shang on 2018/8/14.
 */
class MediaPlayerController {

    companion object {
        val TAG = "MediaPlayerController"

        lateinit var mediaPlayer:MediaPlayer
        var currentTime:Int=0
        var playState:Boolean=false
        var index:Int=0


        //可以考慮改用When 加上動作Int 這樣就可以不用這麼多fun了 缺點是會混再一起

        fun play(path:String){
            if(playState && path==null){
                stopMediaPlayer()
            }else{
                startMediaPlayer(path)
            }
        }

        fun startMediaPlayer(path: String) {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener {
                if (it != null) {
                    it.start()
                    var time = mediaPlayer.duration / 1000
                    var minute = time / 60
                    var second = time - minute * 60
                    Log.d(TAG, "$minute 分/$second 秒")
                }
            }

            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }
        }

        fun stopMediaPlayer() {
            if(mediaPlayer!=null && mediaPlayer.isPlaying){
                currentTime = mediaPlayer.currentPosition
                mediaPlayer.pause()
            }
            Log.d(TAG, "pause()")
        }

        fun reStartMediaPlayer() {
            if(mediaPlayer!=null){
                mediaPlayer.seekTo(currentTime)
                mediaPlayer.start()
            }

            Log.d(TAG, "reStart()")
        }

        fun resetMediaPlayer(){
            if(mediaPlayer!=null && mediaPlayer.isPlaying){
                mediaPlayer.reset()
            }
        }

        fun releaseMediaPlayer(){
            if(mediaPlayer!=null)
                mediaPlayer.release()
        }

    }


}