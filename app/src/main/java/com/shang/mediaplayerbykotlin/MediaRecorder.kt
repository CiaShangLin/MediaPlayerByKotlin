package com.shang.mediaplayerbykotlin

import android.media.MediaRecorder
import android.util.Log
import com.shang.mediaplayerbykotlin.MP.MPC
import kotlin.math.log10

/**
 * Created by Shang on 2018/9/5.
 */
class MediaRecorder(){



    companion object {
       private val mediaRecorder:MediaRecorder by lazy {
            MediaRecorder().apply {
                this.setAudioSource(MediaRecorder.AudioSource.MIC)
                this.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                this.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                this.setOutputFile("/dev/null")
                this.prepare()
                this.start()
            }
        }




        fun getAmplitude(){
            try{
                var db= 20 * log10(mediaRecorder.maxAmplitude.toDouble())
                Log.d("MediaRecorder", db.toString())

            }catch (e:Exception){

            }
        }
    }


}