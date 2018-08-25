package com.shang.mediaplayerbykotlin.MP

/**
 * Created by Shang on 2018/8/19.
 */
interface MPC_Interface{

    companion object {
        val CURRENT_TIME="CURRENT_TIME"
        val PATH:String="PATH"
        val NAME:String="NAME"
        val DURATION: String = "DURATION"
    }


    fun start()
    fun stop()
    fun reset()
    fun next()
    fun previous()
    fun release()
    fun reStart()
    fun getName():String
}