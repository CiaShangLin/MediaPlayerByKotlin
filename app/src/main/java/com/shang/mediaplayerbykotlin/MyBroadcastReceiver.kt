package com.shang.mediaplayerbykotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.shang.mediaplayerbykotlin.Activity.MainActivity
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity

//接收廣播 發送UI操作
open class MyBroadcastReceiver(var myBroadcastReceiverUI: MyBroadcastReceiverUI) : BroadcastReceiver() {

    companion object {
        val PLAY: String = "PLAY"
        val START: String = "START"
        val PAUSE: String = "PAUSE"
        val RESTART: String = "RESTART"
        val NEXT: String = "NEXT"
        val PREVIOUS: String = "PREVIOUS"
        val MODE: String = "MODE"
        val REPEAT: String = "REPEAT"
        val INSERT: String = "INSERT"
        val SEEKBAR_MOVE: String = "SEEKBAR_MOVE"
        val LOOPING: String = "LOOPING"
        val CURRENT_TIME: String = "CURRENT_TIME"
        val RESTORE: String = "RESTORE"

        fun getIntentFilter(context: Context):IntentFilter{
            when(context){
                is MainActivity ->{
                    return IntentFilter().apply {
                        this.addAction(START)
                        this.addAction(PAUSE)
                        this.addAction(RESTART)
                    }
                }
                is PlayMusicActivity->{
                    return IntentFilter().apply {
                        this.addAction(PLAY)
                        this.addAction(START)
                        this.addAction(PAUSE)
                        this.addAction(NEXT)
                        this.addAction(PREVIOUS)
                        this.addAction(RESTART)
                        this.addAction(MODE)
                        this.addAction(LOOPING)
                        this.addAction(CURRENT_TIME)
                        this.addAction(RESTORE)
                    }
                }
                else->{
                    return IntentFilter()
                }
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {


        when (intent?.action) {
            START -> {
                myBroadcastReceiverUI.start(intent)
            }
            RESTART -> {
                myBroadcastReceiverUI.reStart()
            }
            PAUSE -> {
                myBroadcastReceiverUI.pause()
            }
            NEXT -> {
                myBroadcastReceiverUI.next()
            }

            PREVIOUS -> {
                myBroadcastReceiverUI.previous()
            }

            LOOPING -> {
                myBroadcastReceiverUI.looping(intent)
            }

            CURRENT_TIME -> {
                myBroadcastReceiverUI.current_time(intent)
            }

            MODE -> {
                myBroadcastReceiverUI.mode(intent)
            }

            RESTORE -> {
                myBroadcastReceiverUI.reStore()
            }
        }

    }
}