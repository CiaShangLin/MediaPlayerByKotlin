package com.shang.mediaplayerbykotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.media.ThumbnailUtils
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import java.text.SimpleDateFormat

class PlayMusicActivity : AppCompatActivity() {


    lateinit var myReceiver: MyReceiver

    //Service用 廣播用
    companion object {
        val TAG = "PlayMusicActivity"
        val PLAY:String="PLAY"
        val START: String = "START"
        val PAUSE: String = "PAUSE"
        val RESTART: String = "RESTART"
        val NEXT: String = "NEXT"
        val PREVIOUS: String = "PREVIOUS"
        val MODE: String = "MODE"
        val REPEAT: String = "REPEAT"
        val INSERT: String = "INSERT"
        val SEEKBAR_MOVE:String="SEEKBAR_MOVE"
        val LOOPING:String="LOOPING"
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

                    var bitmap=BitmapFactory.decodeFile(MPC.musicList.get(MPC.index).picture)
                    if(bitmap==null){
                        playmusicIg.setImageResource(R.drawable.ic_music)
                    }else{
                        playmusicIg.setImageBitmap(bitmap)
                    }

                    playerBt.setImageResource(R.drawable.ic_pause)
                }

                PAUSE->{
                    playerBt.setImageResource(R.drawable.ic_play)
                }

                RESTART->{
                    playerBt.setImageResource(R.drawable.ic_pause)
                }

                NEXT->{
                    toast("最後一首了")
                }

                PREVIOUS->{
                    toast("已經是第一首")
                }

                LOOPING->{
                    toast(intent.getStringExtra("status"))
                }

                CURRENT_TIME->{
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

        myReceiver = MyReceiver()

        play_music_bar.setNavigationIcon(R.drawable.ic_back)
        play_music_bar.title = ""
        setSupportActionBar(play_music_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        playerBt.setOnClickListener {
            startService(Intent(this, MediaPlayerService::class.java).apply {
                this.action = PLAY
            })
        }

        nextBt.setOnClickListener {
            startService(Intent(this,MediaPlayerService::class.java).apply {
                this.action= NEXT
            })
        }

        previousBt.setOnClickListener {
            startService(Intent(this,MediaPlayerService::class.java).apply {
                this.action= PREVIOUS
            })
        }

        repeatBt.setOnClickListener {
            startService(Intent(this,MediaPlayerService::class.java).apply {
                this.action= REPEAT
            })
            if(MPC.mediaPlayer!=null && MPC.mediaPlayer!!.isLooping){
                repeatBt.setImageResource(R.drawable.ic_repeat_nofocus)
            }else{
                repeatBt.setImageResource(R.drawable.ic_repeat_focus)
            }
        }

        randomBt.setOnClickListener {


        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                startService(Intent(this@PlayMusicActivity,MediaPlayerService::class.java).apply {
                    this.action= SEEKBAR_MOVE
                    this.putExtra(SEEKBAR_MOVE,seekBar!!.progress)
                })
            }

        })

        if(MPC.mediaPlayer==null){
            Log.d(TAG,"正常播放")
            MPC.index=intent.getIntExtra("index",0)
            startService(Intent(this, MediaPlayerService::class.java).apply {
                this.action = PLAY
            })
        }else{
            Log.d(TAG,"插播")
            MPC.index=intent.getIntExtra("index",0)
            startService(Intent(this, MediaPlayerService::class.java).apply {
                this.action = INSERT
            })
        }

    }

    fun getTimeFormat(duration:Int):String{
        return SimpleDateFormat("mm:ss").format(duration)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
        var intentFilter=IntentFilter().apply {
            this.addAction(PLAY)
            this.addAction(START)
            this.addAction(PAUSE)
            this.addAction(NEXT)
            this.addAction(PREVIOUS)
            this.addAction(RESTART)
            this.addAction(MODE)
            this.addAction(LOOPING)
            this.addAction(CURRENT_TIME)
        }
        registerReceiver(myReceiver, intentFilter)
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
