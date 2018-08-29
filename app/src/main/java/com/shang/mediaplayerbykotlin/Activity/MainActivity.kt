package com.shang.mediaplayerbykotlin.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.shang.mediaplayerbykotlin.Adapter.MusicDataAdapter
import com.shang.mediaplayerbykotlin.Adapter.PlayListNameAdapter
import com.shang.mediaplayerbykotlin.CheckFileRoom
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import com.shang.mediaplayerbykotlin.Notification
import com.shang.mediaplayerbykotlin.R
import com.shang.mediaplayerbykotlin.Room.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File


class MainActivity : AppCompatActivity() {

    val TAG = "Music"

    val database: MusicDatabase by lazy{
        MusicDatabase.getMusicDatabase(this)
    }
    lateinit var adapter: MusicDataAdapter

    var handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            when (msg?.what) {
                MusicDataAdapter.DATABASE_SUCCCESS -> {


                    adapter = MusicDataAdapter(this@MainActivity, MPC.musicList)
                    recyclerview.adapter = adapter
                }


            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate")


        initView()

        var readPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        var writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        if (readPermission && writePermission) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        } else {
            CheckFileRoom(this).execute()
        }


    }

    fun initView() {

        setSupportActionBar(toolbar)
        toolbar.title = "我的音樂"
        toolbar.setNavigationIcon(R.drawable.ic_navigation)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            when (it.itemId) {

                R.id.myMusic -> {
                    doAsync {
                        var list = database.getMusic_Data_Dao().getAll()
                        var setting=database.getSetting_Dao().getSetting()

                        MPC.musicList = list
                        MPC.sort(setting.sort_mode,setting.sort_type)
                        uiThread {
                            recyclerview.adapter = MusicDataAdapter(this@MainActivity, MPC.musicList)
                        }
                    }
                }
                R.id.favorite -> {

                }
                R.id.musicList -> {
                    doAsync {
                        var playList = mutableListOf<Music_ListName_Entity>()
                        playList.addAll(database.getMusic_ListName_Dao().getAll())
                        uiThread {
                            recyclerview.adapter = PlayListNameAdapter(this@MainActivity, playList)
                        }
                    }
                }
                R.id.timer -> {
                    Handler().postDelayed(Runnable {
                        startService(Intent(this,MediaPlayerService::class.java).apply {
                            this.action=PlayMusicActivity.PAUSE
                        })
                    },40*60*1000)
                }
            }

            true
        }

        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerview.setHasFixedSize(true)

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
                var settingDao = database.getSetting_Dao()
                var settingEntity = settingDao.getSetting()

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

                    doAsync {

                        database.getSetting_Dao().update(Setting_Entity().apply {
                            this.name = Setting_Entity.key
                            this.sort_mode = mode
                            this.sort_type = type
                        })

                        MPC.sort(mode, type)
                        uiThread {
                            adapter.notifyDataSetChanged()
                        }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var flag = true
        if (requestCode == 1) {
            grantResults.forEach {
                if (it != PackageManager.PERMISSION_GRANTED) {
                    flag = false
                }
            }

            if (flag) {
                CheckFileRoom(this).execute()
            } else {
                AlertDialog.Builder(this).setMessage("滾").show()
            }

        }
    }

}


