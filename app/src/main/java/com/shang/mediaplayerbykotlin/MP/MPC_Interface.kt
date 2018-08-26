package com.shang.mediaplayerbykotlin.MP

/**
 * Created by Shang on 2018/8/19.
 */
interface MPC_Interface{

    //intent傳遞資料用
    companion object {
        val CURRENT_TIME="CURRENT_TIME"
        val PATH:String="PATH"
        val NAME:String="NAME"
        val DURATION: String = "DURATION"
    }


    fun play()
    fun start()
    fun pause()
    fun next()
    fun next(index:Int)
    fun previous()
    fun release()
    fun reStart()
    fun setLooping()
    fun seekbar_move(time:Int)
    fun getName():String
}