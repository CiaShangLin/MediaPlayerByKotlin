package com.shang.mediaplayerbykotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.shang.mediaplayerbykotlin.Adapter.PlayListDataAdapter
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_normal

import kotlinx.android.synthetic.main.activity_play_list.*

class PlayListActivity : AppCompatActivity() {

    val TAG = "PlayListActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)

        MPC.musicList.forEach {
            Log.d(TAG, it.name + " " + it.path + " " + it.modified)
        }

        play_list_recyc.layoutManager=LinearLayoutManager(this)
        play_list_recyc.adapter=PlayListDataAdapter(this,MPC.musicList)
    }

}
