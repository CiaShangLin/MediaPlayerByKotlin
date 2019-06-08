package com.shang.mediaplayerbykotlin.Activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.MP.MPC_normal
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import com.shang.mediaplayerbykotlin.NotificationUnits
import com.shang.mediaplayerbykotlin.R
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlinx.android.synthetic.main.media_play_controller.*
import kotlinx.android.synthetic.main.media_player.*
import org.jetbrains.anko.startService
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat

class PlayMusicActivity : AppCompatActivity() {

    //Service用 廣播用
    companion object {
        lateinit var myReceiver: MyReceiver
        val TAG = "PlayMusicActivity"
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

                    var bitmap = BitmapFactory.decodeFile(MPC.musicList.get(MPC.index).picture)
                    if (bitmap == null) {
                        playmusicIg.setImageResource(R.drawable.ic_music)
                    } else {
                        playmusicIg.setImageBitmap(bitmap)
                    }

                    playerBt.setImageResource(R.drawable.ic_pause)

                    NotificationUnits.instance(this@PlayMusicActivity).update(this@PlayMusicActivity,
                            MPC.musicList.get(MPC.index).name,
                            MPC.musicList.get(MPC.index).picture)
                }

                PAUSE -> {
                    playerBt.setImageResource(R.drawable.ic_play_button)
                    NotificationUnits.instance(this@PlayMusicActivity).update(this@PlayMusicActivity,
                            MPC.musicList.get(MPC.index).name,
                            MPC.musicList.get(MPC.index).picture)
                }

                RESTART -> {
                    playerBt.setImageResource(R.drawable.ic_pause)
                    NotificationUnits.instance(this@PlayMusicActivity).update(this@PlayMusicActivity,
                            MPC.musicList.get(MPC.index).name,
                            MPC.musicList.get(MPC.index).picture)
                }

                NEXT -> {
                    toast("最後一首了")
                }

                PREVIOUS -> {
                    toast("已經是第一首")
                }

                LOOPING -> {
                    toast(intent.getStringExtra(MPC_Interface.STATUS))
                }

                CURRENT_TIME -> {
                    var duration: Int = intent.getIntExtra(MPC_Interface.CURRENT_TIME, 0)
                    seekBar.progress = duration
                    startTimeTv.text = getTimeFormat(duration)
                }

                MODE -> {
                    var status = intent.getBooleanExtra(MODE, false)
                    if (status) {
                        randomBt.setImageResource(R.drawable.ic_random_focus)
                        toast("隨機模式打開")
                    } else {
                        randomBt.setImageResource(R.drawable.ic_random_nofocus)
                        toast("隨機模式關閉")
                    }
                }

                RESTORE -> {
                    Log.d(TAG, "回復")
                    seekBar.max = MPC.musicList.get(MPC.index).duration.toInt()
                    seekBar.progress = MPC.currentTime

                    startTimeTv.text = getTimeFormat(MPC.currentTime)
                    endTimeTv.text = getTimeFormat(MPC.musicList.get(MPC.index).duration.toInt())

                    nameTv.text = MPC.musicList.get(MPC.index).name

                    var bitmap = BitmapFactory.decodeFile(MPC.musicList.get(MPC.index).picture)
                    if (bitmap == null) {
                        playmusicIg.setImageResource(R.drawable.ic_music)
                    } else {
                        playmusicIg.setImageBitmap(bitmap)
                    }

                    if (MPC.mediaPlayer != null && MPC.mediaPlayer!!.isPlaying) {
                        playerBt.setImageResource(R.drawable.ic_pause)
                    } else {
                        playerBt.setImageResource(R.drawable.ic_play)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)


        Log.d(TAG, "onCreate")

        myReceiver = MyReceiver()

        //進入畫面
        var playIndex = intent.getIntExtra(MPC_Interface.INDEX, 0)
        if (MPC.index != playIndex) {  //點到跟現在不同的 用PLAY的話她會啟動到RESTART
            MPC.index = playIndex
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = INSERT
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        } else {  //恢復
            var intent=Intent(this, MediaPlayerService::class.java).apply {
                action = RESTORE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        setSupportActionBar(play_music_bar)
        play_music_bar.setNavigationIcon(R.drawable.ic_back)
        play_music_bar.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        playerBt.setOnClickListener {
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = PLAY
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        nextBt.setOnClickListener {
            var intent=Intent(this, MediaPlayerService::class.java).apply {
                this.action = NEXT
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }else{
                startService(intent)
            }
        }

        previousBt.setOnClickListener {
            var intent=Intent(this, MediaPlayerService::class.java).apply {
                this.action = PREVIOUS
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }else{
                startService(intent)
            }
        }

        repeatBt.setOnClickListener {
            startService(Intent(this, MediaPlayerService::class.java).apply {
                this.action = REPEAT
            })
            if (MPC.mediaPlayer != null && MPC.mediaPlayer!!.isLooping) {
                repeatBt.setImageResource(R.drawable.ic_repeat_nofocus)
            } else {
                repeatBt.setImageResource(R.drawable.ic_repeat_focus)
            }
        }

        randomBt.setOnClickListener {
            var intent=Intent(this, MediaPlayerService::class.java).apply {
                this.action = MODE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }else{
                startService(intent)
            }
        }
        try {
            if (MPC.mpc_mode is MPC_normal) {
                randomBt.setImageResource(R.drawable.ic_random_nofocus)
            } else {
                randomBt.setImageResource(R.drawable.ic_random_focus)
            }
        } catch (e: Exception) {  //會噴出未定義錯誤
            randomBt.setImageResource(R.drawable.ic_random_nofocus)
        }



        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                var intent=Intent(this@PlayMusicActivity, MediaPlayerService::class.java).apply {
                    this.action = SEEKBAR_MOVE
                    this.putExtra(SEEKBAR_MOVE, seekBar!!.progress)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                }else{
                    startService(intent)
                }
            }
        })


    }

    fun getTimeFormat(duration: Int): String {
        return SimpleDateFormat("mm:ss").format(duration)
    }

    override fun onResume() {
        super.onResume()
        play_music_bar.title = ""
        var intentFilter = IntentFilter().apply {
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
