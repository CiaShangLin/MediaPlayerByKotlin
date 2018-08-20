package com.shang.mediaplayerbykotlin

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import com.shang.mediaplayerbykotlin.Room.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.mediapalyer_controller_ui.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import java.io.File


class MainActivity : AppCompatActivity() {


    val TAG = "Music"

    lateinit var database: MusicDatabase
    lateinit var file: MutableList<File>



    var handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            Log.d("Music", msg?.what.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //耗時工作
        AsyncTask.execute {
            FileUnits.findAllMusic(File(Environment.getExternalStorageDirectory().toString()))
            file = FileUnits.musicList
            Log.d(TAG, file.size.toString())
            MPC.musicList=file



            database=MusicDatabase.getMusicDatabase(this)
            var list=database.getMusic_Data_Dao().getAll()
            Log.d(TAG,list.size.toString())
            Log.d(TAG," "+list.get(0).name)

        }

        initView()



    }

    fun initView(){

        seekBar.progress = 0
        seekBar.max = 100000
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                nameTv.text = seekBar?.progress.toString()
            }

        })

        setSupportActionBar(toolbar)
        toolbar.title="我的音樂"
        toolbar.setNavigationIcon(R.drawable.ic_navigation)

        val toggle=ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.favorite->{
                    drawerLayout.closeDrawers()
                    startService(Intent(this,MediaPlayerService::class.java).apply {
                        action="START"
                        putExtra("path",file.get(0).path)
                    })

                }
                R.id.musicList->{
                    drawerLayout.closeDrawers()

                    startService(Intent(this,MediaPlayerService::class.java).apply {
                        action="STOP"
                    })

                }
                R.id.timer->{

                    startService(Intent(this,MediaPlayerService::class.java).apply {
                        action="MODE"
                    })

                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.search -> {

            true
        }

        R.id.sort -> {

            true
        }

        else -> {

            super.onOptionsItemSelected(item)
        }
    }

}


