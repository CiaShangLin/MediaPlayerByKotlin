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
        val INDEX:String="INDEX"
        val ID:String="ID"
        val STATUS:String="STATUS"

    }


    fun play()
    fun start()
    fun pause()
    fun next()
    fun insert()
    fun previous()
    fun release()
    fun reStart()
    fun setLooping()
    fun seekbar_move(time:Int)
    fun getName():String
}