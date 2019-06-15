package com.shang.mediaplayerbykotlin.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shang.mediaplayerbykotlin.*
import com.shang.mediaplayerbykotlin.Adapter.MusicDataAdapter
import com.shang.mediaplayerbykotlin.Adapter.PlayListNameAdapter
import com.shang.mediaplayerbykotlin.Broadcast.MyBroadcastReceiver
import com.shang.mediaplayerbykotlin.Broadcast.MyBroadcastReceiverUI
import com.shang.mediaplayerbykotlin.Dialog.LoadDialog
import com.shang.mediaplayerbykotlin.Dialog.TimerDialog
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

    private val TAG: String = "MainActivity"
    lateinit var adapterMain: MusicDataAdapter
    lateinit var adapterListName: PlayListNameAdapter
    private val loadDialog: LoadDialog by lazy { LoadDialog() }
    private val mediaPlayerModel: MediaPlayerModel by lazy { ViewModelProviders.of(this).get(MediaPlayerModel::class.java) }

    private val mLocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }
    private var myBroadcastReceiverUI = object : MyBroadcastReceiverUI {
        override fun start(intent: Intent) {
            Log.v(TAG, "START")
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

        override fun pause() {
            Log.v(TAG, "PAUSE")
            simpleBt.setImageResource(R.drawable.ic_remote_play)
        }

        override fun reStart() {
            Log.v(TAG, "RESTART")
            simpleBt.setImageResource(R.drawable.ic_remote_pause)
        }
    }
    private var myBroadcastReceiver = MyBroadcastReceiver(myBroadcastReceiverUI)


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
        initModel()

    }

    private fun initView() {
        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_navigation)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                //未實作
                R.id.search -> {


                }
                //要修改
                R.id.sort -> {
                    var popupMenu = MyPopupMenu(this, findViewById<View>(R.id.sort), R.menu.sort_menu, mediaPlayerModel)
                    popupMenu.setOnMenuItemClickListener(popupMenu)
                    popupMenu.show()
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
                    recyclerview.adapter=adapterMain
                    toolbar.title="我的音樂"
                    toolbar.menu.findItem(R.id.sort).isVisible = true
                }
                R.id.favorite -> {


                }
                R.id.musicList -> {
                    mediaPlayerModel.getPlayListNameLiveData().value = true
                    toolbar.title="播放清單"
                    toolbar.menu.findItem(R.id.sort).isVisible = false
                }
                R.id.timer -> {
                    var timerDialog = TimerDialog()
                    timerDialog.show(fragmentManager, "TimerDialog")
                }
            }
            true
        }


        //recyclerview
        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerview.setHasFixedSize(true)
        adapterMain = MusicDataAdapter(this, mutableListOf())
        recyclerview.adapter = adapterMain

        //撥放按鈕
        simpleBt.setOnClickListener {
            var intent = Intent(this, MediaPlayerService::class.java).apply {
                this.action = MyBroadcastReceiver.PLAY
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        //整個simple的Layout
        simple_conLy.setOnClickListener {
            //一開始是沒有資料的 所以傳遞過去會閃退
            if(MPC.index!=-1){
                startActivity(Intent(this, PlayMusicActivity::class.java).apply {
                    this.putExtra(MPC_Interface.INDEX, MPC.index)
                })
            }
        }
    }

    private fun initModel() {
        //Setting
        mediaPlayerModel?.getSettingLiveData().observe(this, Observer<Setting_Entity> {
            //應該要寫預設插入
            if (it != null) {
                mediaPlayerModel.getAllMusicData()
                        .postValue(mediaPlayerModel.getAllMusicDataOrderBy(it.sort_mode, it.sort_type))
            } else {
                mediaPlayerModel.insertSetting(Setting_Entity())
            }
        })

        //MusicData
        mediaPlayerModel.getAllMusicData().observe(this, Observer<MutableList<Music_Data_Entity>> {
            adapterMain.setData(it)
            MPC.musicList = it
            Log.d(TAG, "mediaPlayer:${it.size}")
        })

        //LoadDailog
        mediaPlayerModel.getLoadStatus().observe(this, Observer {
            if (it) {
                loadDialog.dismiss()
            } else {
                loadDialog.show(supportFragmentManager, LoadDialog.TAG)
            }
        })

        //PlayListName
        mediaPlayerModel.getPlayListNameLiveData().observe(this, Observer {
            Log.d(TAG, "getPlayListNameLiveData:$it")
            if (it) {
                adapterListName = PlayListNameAdapter(this, mediaPlayerModel.getAllListName())
                recyclerview.adapter = adapterListName
                adapterListName.notifyDataSetChanged()
            }
        })


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
        mLocalBroadcastManager.registerReceiver(myBroadcastReceiver, MyBroadcastReceiver.getIntentFilter(this))
    }


    override fun onDestroy() {

        super.onDestroy()
        Log.d("TAG", "onDestroy")
        mLocalBroadcastManager.unregisterReceiver(myBroadcastReceiver)
        stopService(Intent(this, MediaPlayerService::class.java))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)  //防止destory
        }
        return super.onKeyDown(keyCode, event)
    }
}


