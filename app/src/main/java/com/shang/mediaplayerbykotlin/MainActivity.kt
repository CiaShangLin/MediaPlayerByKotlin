package com.shang.mediaplayerbykotlin

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageStats
import android.media.MediaPlayer
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import com.shang.mediaplayerbykotlin.Room.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File
import java.util.jar.Manifest
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {


    val TAG = "Music"

    lateinit var database: MusicDatabase
    lateinit var file: MutableList<File>


    var handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            when (msg?.what) {
                MusicAdapter.DATABASE_SUCCCESS -> {
                    MPC.musicList=msg.obj as MutableList<Music_Data_Entity>
                    MPC.musicList.sortByDescending {
                        it.modified
                    }
                    recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerview.adapter = MusicAdapter(this@MainActivity, MPC.musicList)

                    database = MusicDatabase.getMusicDatabase(this@MainActivity)
                    initView()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreate")

        //耗時工作
        CheckFileRoom(this).execute()

        var readPermission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
        var writePermission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED


        if(!readPermission && !writePermission){
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        }else{
            CheckFileRoom(this).execute()
        }



        /*AsyncTask.execute {
            database = MusicDatabase.getMusicDatabase(this)
            initView()
        }*/


    }

    fun initView() {

        setSupportActionBar(toolbar)
        toolbar.title = "我的音樂"
        toolbar.setNavigationIcon(R.drawable.ic_navigation)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.myMusic -> {
                    doAsync {
                        var list = database.getMusic_Data_Dao().getAll()
                        uiThread {
                            recyclerview.adapter = MusicAdapter(it, list)
                        }
                    }
                }
                R.id.favorite -> {


                }
                R.id.musicList -> {

                    AsyncTask.execute {
                        var playList = mutableListOf<Music_ListName_Entity>()
                        playList.addAll(database.getMusic_ListName_Dao().getAll())
                        runOnUiThread {
                            recyclerview.adapter = PlayListAdapter(this, playList)
                        }
                    }
                }
                R.id.timer -> {

                }
            }
            drawerLayout.closeDrawers()
            true
        }

        /*AsyncTask.execute {
            recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerview.adapter = MusicAdapter(this@MainActivity, database.getMusic_Data_Dao().getAll())
        }*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.search -> {
            startActivity(Intent(this,PlayMusicActivity::class.java))
            true
        }

        R.id.sort -> {

            var view = findViewById<View>(R.id.sort)
            var popupMenu = PopupMenu(this, view)
            var inf = popupMenu.menuInflater
            inf.inflate(R.menu.sort_menu, popupMenu.menu)

            AsyncTask.execute {
                var settingDao = database.getSetting_Dao()
                var settingEntity = settingDao.getSetting()

                if (settingEntity == null) {  //第一次使用
                    settingDao.insertSetting(Setting_Entity())
                    settingEntity = settingDao.getSetting()
                }

                var mode: Boolean = settingEntity.sort_mode
                var type: Int = settingEntity.sort_type
                Log.d(TAG, mode.toString() + " " + type)

                popupMenu.menu.findItem(R.id.sort_mode).setChecked(mode)
                popupMenu.menu.getItem(type).setChecked(true)

                popupMenu?.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.sort_mode -> {
                            mode = !mode
                            it.setChecked(mode)
                        }
                        R.id.sort_modify -> {
                            type = 1
                            it.setChecked(true)
                        }
                        R.id.sort_name -> {
                            type = 2
                            it.setChecked(true)
                        }
                        R.id.sort_time -> {
                            type = 3
                            it.setChecked(true)
                        }
                    }

                    AsyncTask.execute {
                        database.getSetting_Dao().update(Setting_Entity().apply {
                            this.name = Setting_Entity.key
                            this.sort_mode = mode
                            this.sort_type = type
                        })
                    }
                    true
                }

                runOnUiThread {
                    popupMenu.show()
                }
            }
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG,"onResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy()")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==1){
            CheckFileRoom(this).execute()
        }
    }
}


