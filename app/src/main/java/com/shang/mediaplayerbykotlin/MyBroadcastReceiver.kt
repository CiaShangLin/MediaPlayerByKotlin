package com.shang.mediaplayerbykotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.shang.mediaplayerbykotlin.Activity.MainActivity
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity


class MyBroadcastReceiver(var myBroadcastReceiverUI: MyBroadcastReceiverUI) : BroadcastReceiver() {

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

        fun getMainActivityIntentFilter(): IntentFilter {
            return IntentFilter().apply {
                this.addAction(START)
                this.addAction(PAUSE)
                this.addAction(RESTART)
            }
        }

        fun getPlayMusicActivityIntentFilter(): IntentFilter {
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
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TAG", (context is MainActivity).toString())
        if (context is MainActivity || context is PlayMusicActivity) {
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
                    myBroadcastReceiverUI.looping()
                }

                CURRENT_TIME -> {
                    myBroadcastReceiverUI.current_time(intent)
                }

                MODE -> {
                    myBroadcastReceiverUI.mode()
                }

                RESTORE -> {
                    myBroadcastReceiverUI.reStore()
                }
            }
        }

    }
}