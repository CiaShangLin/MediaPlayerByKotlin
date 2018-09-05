package com.shang.mediaplayerbykotlin.Activity

import android.app.ProgressDialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateFormat.getTimeFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import com.shang.mediaplayerbykotlin.*
import com.shang.mediaplayerbykotlin.Adapter.MusicDataAdapter
import com.shang.mediaplayerbykotlin.Adapter.PlayListDataAdapter
import com.shang.mediaplayerbykotlin.Adapter.PlayListNameAdapter
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import com.shang.mediaplayerbykotlin.Room.*
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.media_play_controller.*
import kotlinx.android.synthetic.main.media_player.*
import kotlinx.android.synthetic.main.music_data_item.*
import kotlinx.android.synthetic.main.sample_controller_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.custom.style
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File


class MainActivity : AppCompatActivity() {

    val TAG = "Music"

    val database: MusicDatabase by lazy {
        MusicDatabase.getMusicDatabase(this)
    }

    companion object {
        val DATABASE_SUCCCESS: String = "DATABASE_SUCCCESS"
        lateinit var model: MPC
    }


    lateinit var adapterMain: MusicDataAdapter
    lateinit var adapterListName: PlayListNameAdapter
    lateinit var loadDialog: LoadDialog


    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action) {
                DATABASE_SUCCCESS -> {
                    adapterMain = MusicDataAdapter(this@MainActivity, MPC.musicList)
                    recyclerview.adapter = adapterMain
                    loadDialog.dismiss()
                }
                PlayMusicActivity.START -> {
                    simpleBt.setImageResource(R.drawable.ic_remote_pause)

                    simpleTitle.text = intent.getStringExtra(MPC_Interface.NAME)
                    simpleTime.text = FileUnits.lastModifiedToSimpleDateFormat(intent.getIntExtra(MPC_Interface.DURATION, 0).toLong())

                    var bitmap = BitmapFactory.decodeFile(MPC.musicList.get(MPC.index).picture)
                    if (bitmap == null) {
                        simpleIg.setImageResource(R.drawable.ic_music)
                    } else {
                        simpleIg.setImageBitmap(bitmap)
                    }
                }
                PlayMusicActivity.PAUSE -> {
                    simpleBt.setImageResource(R.drawable.ic_remote_play)
                }
                PlayMusicActivity.RESTART -> {
                    simpleBt.setImageResource(R.drawable.ic_remote_pause)
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

        model = ViewModelProviders.of(this).get(MPC::class.java)
        model.getLiveData().observe(this, Observer {
            MPC.musicList=it!!
            adapterMain.musicList=it!!
            adapterMain.notifyDataSetChanged()
        })

        loadDialog = LoadDialog()
        loadDialog.show(fragmentManager, "LoadingDialog")

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_navigation)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            when (it.itemId) {

                R.id.myMusic -> {
                    var list = database.getMusic_Data_Dao().getAll()
                    var setting = database.getSetting_Dao().getSetting()

                    MPC.musicList = list
                    MPC.sort(setting.sort_mode, setting.sort_type)

                    recyclerview.adapter = adapterMain

                }
                R.id.favorite -> {


                }
                R.id.musicList -> {

                    var playList = mutableListOf<Music_ListName_Entity>()
                    playList.addAll(database.getMusic_ListName_Dao().getAll())

                    adapterListName = PlayListNameAdapter(this@MainActivity, playList)
                    recyclerview.adapter = adapterListName

                }
                R.id.timer -> {
                    var timerDialog = TimerDialog()
                    timerDialog.show(fragmentManager, "TimerDialog")
                }
            }

            true
        }

        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerview.setHasFixedSize(true)

        simpleBt.setOnClickListener {
            startService(Intent(this, MediaPlayerService::class.java).apply {
                this.action = PlayMusicActivity.PLAY
            })
        }

        simple_conLy.setOnClickListener {
            startActivity(Intent(this, PlayMusicActivity::class.java).apply {
                this.putExtra(MPC_Interface.INDEX, MPC.index)
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.search -> {
            //model.getLiveData().value= database.getMusic_Data_Dao().test()
            MediaRecorder.getAmplitude()
            true
        }

        R.id.sort -> {

            var view = findViewById<View>(R.id.sort)
            var popupMenu = PopupMenu(this, view)
            var inf = popupMenu.menuInflater
            inf.inflate(R.menu.sort_menu, popupMenu.menu)


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


                database.getSetting_Dao().update(Setting_Entity().apply {
                    this.name = Setting_Entity.key
                    this.sort_mode = mode
                    this.sort_type = type
                })

                MPC.sort(mode, type)


                Log.d(TAG, "sort")

                adapterMain.notifyDataSetChanged()


                true
            }


            popupMenu.show()


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

    override fun onStart() {
        super.onStart()

        var intentFilter = IntentFilter().apply {
            this.addAction(PlayMusicActivity.START)
            this.addAction(PlayMusicActivity.PAUSE)
            this.addAction(PlayMusicActivity.RESTART)
            this.addAction(DATABASE_SUCCCESS)
        }
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(broadcastReceiver)
    }
}


