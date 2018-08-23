package com.shang.mediaplayerbykotlin

import android.content.Intent
import android.os.*
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import com.shang.mediaplayerbykotlin.Room.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.jetbrains.anko.toast
import java.io.File


class MainActivity : AppCompatActivity() {


    val TAG = "Music"

    lateinit var database: MusicDatabase
    lateinit var file: MutableList<File>



    var handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            when (msg?.what) {
                MusicAdapter.DATABASE_SUCCCESS -> {
                    recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerview.adapter = MusicAdapter(this@MainActivity, msg.obj as MutableList<Music_Data_Entity>)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //耗時工作
        //CheckFileRoom(this).execute()

        AsyncTask.execute {
            database = MusicDatabase.getMusicDatabase(this)
            initView()
        }



    }

    fun initView() {

        /*seekBar.progress = 0
        seekBar.max = 100000
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                nameTv.text = seekBar?.progress.toString()
            }

        })*/

        setSupportActionBar(toolbar)
        toolbar.title = "我的音樂"
        toolbar.setNavigationIcon(R.drawable.ic_navigation)


        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.favorite -> {


                }
                R.id.musicList -> {
                    AsyncTask.execute {
                        var playList= mutableListOf<Music_ListName_Entity>()
                        playList.addAll(database.getMusic_ListName_Dao().getAll())

                        runOnUiThread{
                            recyclerview.adapter=PlayListAdapter(this,playList)
                        }
                    }

                }
                R.id.timer -> {

                }
            }
            drawerLayout.closeDrawers()
            true
        }

        AsyncTask.execute {
            recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerview.adapter = MusicAdapter(this@MainActivity, database.getMusic_Data_Dao().getAll())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.search -> {

            true
        }

        R.id.sort -> {

            var view = findViewById<View>(R.id.sort)
            var popupMenu = PopupMenu(this, view)
            var inf = popupMenu.menuInflater
            inf.inflate(R.menu.sort_menu, popupMenu.menu)

            AsyncTask.execute {
                var settingDao=database.getSetting_Dao()
                var settingEntity=settingDao.getSetting()

                if(settingEntity==null){  //第一次使用
                    settingDao.insertSetting(Setting_Entity())
                    settingEntity=settingDao.getSetting()
                }

                var mode: Boolean = settingEntity.sort_mode
                var type: Int = settingEntity.sort_type
                Log.d(TAG,mode.toString()+" "+type)

                popupMenu.menu.findItem(R.id.sort_mode).setChecked(mode)
                popupMenu.menu.getItem(type).setChecked(true)

                popupMenu?.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.sort_mode -> {
                            it.setChecked(false)
                            toast(it.title.toString())
                        }
                        R.id.sort_modify -> {
                            toast(it.title.toString())
                        }
                        R.id.sort_name -> {
                            toast(it.title.toString())
                        }
                        R.id.sort_time -> {
                            toast(it.title.toString())
                        }
                    }
                    true
                }

                runOnUiThread{
                    popupMenu.show()
                }
            }


            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}


