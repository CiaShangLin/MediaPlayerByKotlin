package com.shang.mediaplayerbykotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlinx.android.synthetic.main.media_play_controller.*

class PlayMusicActivity : AppCompatActivity() {

    val TAG = "PlayMusicActivity"
    lateinit var myReceiver: MyReceiver

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Log.d(TAG, intent.action)
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
                this.action = "START"
                this.putExtra("path", MPC.musicList.get(MPC.index).path)
            })
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
        registerReceiver(myReceiver, IntentFilter(getString(R.string.MyRecevier)))
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
