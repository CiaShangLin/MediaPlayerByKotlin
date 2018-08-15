package com.shang.mediaplayerbykotlin

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_Data
import com.shang.mediaplayerbykotlin.Room.Music_List_Entity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.File


class MainActivity : AppCompatActivity() {


    val TAG = "Music"
    lateinit var mediaPlayer: MediaPlayer
    lateinit var database:MusicDatabase

    var handler=object :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            Log.d("Music",msg?.what.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var file: MutableList<File> = FileUnits().getmusicList()

        button.setOnClickListener {


            startService(Intent(this,MediaPlayerService::class.java).apply {
                action="START"
                putExtra("path",file.get(0).path)
            })


        }

        button2.setOnClickListener {
            startService(Intent(this,MediaPlayerService::class.java).apply {
                action="STOP"
            })
        }

        button3.setOnClickListener {
            startService(Intent(this,MediaPlayerService::class.java).apply {
                action="RESTART"
            })
        }

        button4.setOnClickListener {

            database= MusicDatabase.getMusicDatabase(this@MainActivity)
            AsyncTask.execute{
                for(i in 1..3){
                    var music_data=Music_Data_Entity()
                    music_data.music_data=Music_Data(i.toString(),i,i.toString(),true)

                    database.getMusic_Data_Dao().insert(music_data)
                }


            }


        }

        button5.setOnClickListener {
            var musicData= Music_Data_Entity().apply {
                this.id=2

            }

            AsyncTask.execute{
                database= MusicDatabase.getMusicDatabase(this@MainActivity)
                database.getMusic_Data_Dao().delete(musicData)


                var r=object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                    }
                }

            }



        }

    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {   //案上一頁會啟動
        super.onDestroy()

        Log.d(TAG, "onDestroy()")
    }


}


