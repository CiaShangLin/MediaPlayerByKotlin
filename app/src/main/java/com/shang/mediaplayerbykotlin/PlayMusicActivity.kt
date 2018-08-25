package com.shang.mediaplayerbykotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlinx.android.synthetic.main.media_play_controller.*
import kotlinx.android.synthetic.main.media_player.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.toast

class PlayMusicActivity : AppCompatActivity() {


    lateinit var myReceiver: MyReceiver

    companion object {
        val TAG = "PlayMusicActivity"
        val START: String = "START"
        val STOP: String = "STOP"
        val RESET: String = "RESET"
        val RESTART: String = "RESTART"
        val NEXT: String = "NEXT"
        val PREVIOUS: String = "PREVIOUS"
        val MODE: String = "MODE"

        val CURRENT_TIME:String="CURRENT_TIME"
    }


    /*廣播步驟
    1.先new一個class繼承BroadcastReceiver()
    2.去AndroidManifest.xml寫一個
    <receiver android:name=".PlayMusicActivity$MyReceiver"
    android:enabled="true">
    <intent-filter>
    <action android:name="@string/MyRecevier"></action>
    </intent-filter>
    </receiver>
    3.在使用Receiver的class裡註冊廣播
    4.記得要新增IntentFilter(getString(R.string.MyRecevier))
    5.註銷廣播*/

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            when (intent.action) {
                START -> {
                    var duration = intent.getIntExtra(MPC_Interface.DURATION, 0)

                    seekBar.progress = 0
                    seekBar.max = duration
                    startTimeTv.text = "0:00"
                    endTimeTv.text = getTimeFormat(duration)
                    nameTv.text = intent.getStringExtra(MPC_Interface.NAME)
                    playmusicIg.setImageBitmap(BitmapFactory.decodeFile(MPC.musicList.get(MPC.index).picture))

                }
                CURRENT_TIME->{
                    Log.d(TAG,intent.getIntExtra(MPC_Interface.CURRENT_TIME,0).toString())
                    var duration:Int=intent.getIntExtra(MPC_Interface.CURRENT_TIME,0)
                    seekBar.progress=duration
                    startTimeTv.text=getTimeFormat(duration)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)


        Log.d(TAG, "onCreate")

        MPC.index=intent.getIntExtra("index",0)

        myReceiver = MyReceiver()

        play_music_bar.setNavigationIcon(R.drawable.ic_back)
        play_music_bar.title = ""
        setSupportActionBar(play_music_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        playerBt.setOnClickListener {
            startService(Intent(this, MediaPlayerService::class.java).apply {
                this.action = START
                this.putExtra(MPC_Interface.PATH, MPC.musicList.get(MPC.index).path)
            })
            if(MPC.mediaPlayer==null || MPC.mediaPlayer!!.isPlaying){
                playerBt.setImageResource(R.drawable.ic_pause)
            }else{
                playerBt.setImageResource(R.drawable.ic_play)
            }
        }

        nextBt.setOnClickListener {}
        previousBt.setOnClickListener { }
        repeatBt.setOnClickListener { }
        randomBt.setOnClickListener { }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //nameTv.text = seekBar?.progress.toString()
            }

        })
    }

    fun getTimeFormat(duration:Int):String{
        var time = duration / 1000
        var minute = time / 60
        var second = time - minute * 60

        return "$minute:$second"
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
        var intentFilter=IntentFilter().apply {
            this.addAction(START)
            this.addAction(STOP)
            this.addAction(NEXT)
            this.addAction(PREVIOUS)
            this.addAction(RESET)
            this.addAction(RESTART)
            this.addAction(MODE)
            this.addAction(CURRENT_TIME)
        }
        registerReceiver(myReceiver, intentFilter)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
        unregisterReceiver(myReceiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}