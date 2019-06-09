package com.shang.mediaplayerbykotlin.Activity


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.shang.mediaplayerbykotlin.*
import com.shang.mediaplayerbykotlin.Adapter.MusicDataAdapter
import com.shang.mediaplayerbykotlin.Adapter.PlayListNameAdapter
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.Music_ListName_Entity
import com.shang.mediaplayerbykotlin.Room.Setting_Entity
import com.shang.mediaplayerbykotlin.ViewModel.MediaPlayerModel
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.sample_controller_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MainActivity : AppCompatActivity() {

    val TAG = "Music"

    companion object {
        val DATABASE_SUCCCESS: String = "DATABASE_SUCCCESS"
    }

    lateinit var adapterMain: MusicDataAdapter
    lateinit var adapterListName: PlayListNameAdapter
    val loadDialog: LoadDialog by lazy { LoadDialog() }
    private val mediaPlayerModel: MediaPlayerModel by lazy { ViewModelProviders.of(this).get(MediaPlayerModel::class.java) }


    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action) {
                DATABASE_SUCCCESS -> {
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

        var readPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        var writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        //檢查權限
        if (readPermission && writePermission) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        } else {
            CheckFileRoom(this, mediaPlayerModel).execute()
        }

        initView()
    }

    fun initView() {
        //loadDialog.show(fragmentManager, LoadDialog.TAG)

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_navigation)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                //未實作
                R.id.search -> {
                    var popupMenu = MyPopupMenu(this, findViewById<View>(R.id.sort), R.menu.sort_menu)
                    popupMenu.show()
                    true
                }

                //要修改
                R.id.sort -> {
                    var settingEntity = mediaPlayerModel.getSettingLiveData().value
                    var mode = settingEntity?.sort_mode
                    var type = settingEntity?.sort_type
                    Log.d(TAG, mode.toString() + " " + type)

                    var view = findViewById<View>(R.id.sort)
                    var popupMenu = PopupMenu(this, view)
                    var inf = popupMenu.menuInflater
                    inf.inflate(R.menu.sort_menu, popupMenu.menu)


                    popupMenu.menu.findItem(R.id.sort_mode).setChecked(mode!!)
                    popupMenu.menu.getItem(type!!).setChecked(true)
                }
            }
            true
        }

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            when (it.itemId) {

                R.id.myMusic -> {
                    //var list = database.getMusic_Data_Dao().getAll()
                    //var setting = database.getSetting_Dao().getSetting()

                    //MPC.musicList = list
                    //MPC.sort(setting.sort_mode, setting.sort_type)

                    recyclerview.adapter = adapterMain

                }
                R.id.favorite -> {


                }
                R.id.musicList -> {

                    var playList = mutableListOf<Music_ListName_Entity>()
                    //playList.addAll(database.getMusic_ListName_Dao().getAll())

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
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = PlayMusicActivity.PLAY
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        simple_conLy.setOnClickListener {
            startActivity(Intent(this, PlayMusicActivity::class.java).apply {
                this.putExtra(MPC_Interface.INDEX, MPC.index)
            })
        }

        //Setting
        mediaPlayerModel?.getSettingLiveData().observe(this, Observer<Setting_Entity> {

        })

        mediaPlayerModel.getAllMusicData().observe(this, Observer<MutableList<Music_Data_Entity>> {
            adapterMain = MusicDataAdapter(this, it)
            recyclerview.adapter = adapterMain
            MPC.musicList = it
        })

        mediaPlayerModel.getLoadStatus().observe(this, Observer {
            if (it) {
                loadDialog.dismiss()
            } else {
                loadDialog.show(supportFragmentManager, LoadDialog.TAG)
            }
        })
        mediaPlayerModel.getLoadStatus().value = false

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
                CheckFileRoom(this, mediaPlayerModel).execute()
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
        Log.d("TAG", "onDestroy")

        unregisterReceiver(broadcastReceiver)
        stopService(Intent(this, MediaPlayerService::class.java))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)  //防止destory
        }
        return super.onKeyDown(keyCode, event)
    }
}


