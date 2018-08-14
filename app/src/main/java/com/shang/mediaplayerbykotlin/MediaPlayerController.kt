package com.shang.mediaplayerbykotlin

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log

/**
 * Created by Shang on 2018/8/14.
 */
class MediaPlayerController {

    companion object {
        val TAG = "MediaPlayerController"

        var mediaPlayer=MediaPlayer()
        var currentTime:Int=0


        fun startMediaPlayer(path: String) {
            mediaPlayer = MediaPlayer()
            mediaPlayer.reset()
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
        }

        fun stopMediaPlayer() {
            currentTime = mediaPlayer.currentPosition
            mediaPlayer.pause()
            Log.d(TAG, "pause()")
        }

        fun reStartMediaPlayer() {
            mediaPlayer.seekTo(currentTime)
            mediaPlayer.start()
            Log.d(TAG, "reStart()")

        }

        fun releaseMediaPlayer(){
            if(mediaPlayer!=null)
                mediaPlayer.release()
        }

    }
}