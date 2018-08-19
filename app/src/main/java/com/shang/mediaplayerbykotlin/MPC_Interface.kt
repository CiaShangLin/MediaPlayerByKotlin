package com.shang.mediaplayerbykotlin

/**
 * Created by Shang on 2018/8/19.
 */
interface MPC_Interface{
    fun start(path:String)
    fun stop()
    fun reset()
    fun next()
    fun previous()
    fun release()
    fun reStart()
    fun getName():String
}