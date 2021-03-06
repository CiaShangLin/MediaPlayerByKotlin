package com.shang.mediaplayerbykotlin.Activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.MP.MPC_normal
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import com.shang.mediaplayerbykotlin.Broadcast.MyBroadcastReceiver
import com.shang.mediaplayerbykotlin.Broadcast.MyBroadcastReceiverUI
import com.shang.mediaplayerbykotlin.R
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlinx.android.synthetic.main.media_play_controller.*
import kotlinx.android.synthetic.main.media_player.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat

class PlayMusicActivity : AppCompatActivity() {

    private val TAG = "PlayMusicActivity"
    private val mLocalBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    private var myBroadcastReceiverUI = object : MyBroadcastReceiverUI {
        override fun start(intent: Intent) {
            Log.v(TAG, "START")
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
        }

        override fun pause() {
            Log.v(TAG, "PAUSE")
            playerBt.setImageResource(R.drawable.ic_play_button)
        }

        override fun reStart() {
            Log.v(TAG, "RESTART")
            playerBt.setImageResource(R.drawable.ic_pause)
        }

        override fun next() {
            toast("最後一首了")
        }

        override fun previous() {
            toast("已經是第一首")
        }

        override fun looping(intent: Intent) {
            toast(intent.getStringExtra(MPC_Interface.STATUS))
        }

        override fun current_time(intent: Intent) {
            var duration: Int = intent.getIntExtra(MPC_Interface.CURRENT_TIME, 0)
            seekBar.progress = duration
            startTimeTv.text = getTimeFormat(duration)
        }

        override fun mode(intent: Intent) {
            var status = intent.getBooleanExtra(MyBroadcastReceiver.MODE, false)
            var imageResource = if (status) R.drawable.ic_random_focus else R.drawable.ic_random_nofocus
            var mode = if (status) "隨機模式打開" else "隨機模式關閉"
            randomBt.setImageResource(imageResource)
            toast(mode)
        }

        override fun reStore() {
            Log.v(TAG, "reStore")
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
    var myBroadcastReceiver = MyBroadcastReceiver(myBroadcastReceiverUI)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)

        //進入畫面
        var playIndex = intent.getIntExtra(MPC_Interface.INDEX, 0)
        if (MPC.index != playIndex) {  //點到跟現在不同的 用PLAY的話她會啟動到RESTART
            MPC.index = playIndex
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = MyBroadcastReceiver.INSERT
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        } else {  //恢復
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                action = MyBroadcastReceiver.RESTORE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        //Toolbar
        play_music_bar.setNavigationIcon(R.drawable.ic_back)
        play_music_bar.setNavigationOnClickListener { finish() }

        playerBt.setOnClickListener {
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = MyBroadcastReceiver.PLAY
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        nextBt.setOnClickListener {
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = MyBroadcastReceiver.NEXT
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        previousBt.setOnClickListener {
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = MyBroadcastReceiver.PREVIOUS
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        repeatBt.setOnClickListener {
            startService(Intent(this, MediaPlayerService::class.java).apply {
                this.action = MyBroadcastReceiver.REPEAT
            })
            if (MPC.mediaPlayer != null && MPC.mediaPlayer!!.isLooping) {
                repeatBt.setImageResource(R.drawable.ic_repeat_nofocus)
            } else {
                repeatBt.setImageResource(R.drawable.ic_repeat_focus)
            }
        }

        randomBt.setOnClickListener {
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = MyBroadcastReceiver.MODE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
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
                var intent = Intent(this@PlayMusicActivity, MediaPlayerService::class.java).apply {
                    this.action = MyBroadcastReceiver.SEEKBAR_MOVE
                    this.putExtra(MyBroadcastReceiver.SEEKBAR_MOVE, seekBar!!.progress)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            }
        })


    }

    fun getTimeFormat(duration: Int): String {
        return SimpleDateFormat("mm:ss").format(duration)
    }

    override fun onStart() {
        super.onStart()
        mLocalBroadcastManager.registerReceiver(myBroadcastReceiver, MyBroadcastReceiver.getIntentFilter(this))

    }

    override fun onStop() {
        super.onStop()
        mLocalBroadcastManager.unregisterReceiver(myBroadcastReceiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
