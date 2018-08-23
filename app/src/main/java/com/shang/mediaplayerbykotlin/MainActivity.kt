package com.shang.mediaplayerbykotlin

import android.content.Intent
import android.os.*
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.SeekBar
import com.shang.mediaplayerbykotlin.Room.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.mediapalyer_controller_ui.*
import kotlinx.android.synthetic.main.play_music_ui_layout.*
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

            when(msg?.what){
                MusicAdapter.DATABASE_SUCCCESS->{
                    recyclerview.layoutManager= LinearLayoutManager(this@MainActivity)
                    recyclerview.adapter=MusicAdapter(this@MainActivity,msg.obj as MutableList<Music_Data_Entity>)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //耗時工作
        //CheckFileRoom(this).execute()


        initView()

        playerBt.setOnClickListener{

            /*var remote=RemoteViews(packageName,R.layout.remote_view_layout)
            remote.setImageViewResource(R.id.remoteIg,R.drawable.ic_music)
            remote.setImageViewResource(R.id.remotePreBt,R.drawable.ic_previous)
            remote.setImageViewResource(R.id.remotePlayBt,R.drawable.ic_remote_play)
            remote.setImageViewResource(R.id.remoteNextBt,R.drawable.ic_next)
            remote.setImageViewResource(R.id.remoteCancelBt,R.drawable.ic_cancel)
            remote.setTextViewText(R.id.remoteNameTv,"Song Name")
            remote.setTextViewText(R.id.remoteTimeTv,"03:14")

            var notification=NotificationCompat.Builder(this).apply {
                this.setSmallIcon(R.drawable.ic_music)
                this.setContent(remote)
            }.build()

            val no=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            no.notify(1,notification)*/
        }
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


        AsyncTask.execute {
            //database=MusicDatabase.getMusicDatabase(this)
            var list= mutableListOf<Music_Data_Entity>()
            list.add(Music_Data_Entity().apply {
                this.name="龍珠超 OP2 - 限界突破×サバイバー"
                this.duration=200000
            })
            list.add(Music_Data_Entity().apply {
                this.name="龍珠超：究極的聖戰（BGM）"
                this.duration=210000
            })
            list.add(Music_Data_Entity().apply {
                this.name="10 Gamble Rumble"
                this.duration=220000
            })


            recyclerview.layoutManager= LinearLayoutManager(this@MainActivity)
            recyclerview.adapter=MusicAdapter(this@MainActivity, list)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.search -> {


            AsyncTask.execute {
                database=MusicDatabase.getMusicDatabase(this)
                var d=database.getMusic_Data_Dao().getAll()

            }

            true
        }

        R.id.sort -> {

            var view=findViewById<View>(R.id.sort)
            var popupMenu=PopupMenu(this,view)
            var inf=popupMenu?.menuInflater
            inf?.inflate(R.menu.sort_menu,popupMenu?.menu)

            popupMenu.menu.findItem(R.id.sort_mode).setChecked(false)
            popupMenu.menu.findItem(R.id.sort_modify).setChecked(false)
            popupMenu.menu.findItem(R.id.sort_name).setChecked(false)
            popupMenu.menu.findItem(R.id.sort_time).setChecked(false)

            popupMenu?.setOnMenuItemClickListener {

                when(it.itemId){
                    R.id.sort_mode->{
                        it.setChecked(false)
                        toast(it.title.toString())
                    }
                    R.id.sort_modify->{toast(it.title.toString())}
                    R.id.sort_name->{toast(it.title.toString())}
                    R.id.sort_time->{toast(it.title.toString())}
                }

                true
            }

            popupMenu?.show()

            true
        }

        else -> {

            super.onOptionsItemSelected(item)
        }
    }

}


