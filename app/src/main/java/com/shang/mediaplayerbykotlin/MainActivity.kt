package com.shang.mediaplayerbykotlin

import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.shang.mediaplayerbykotlin.Room.*
import kotlinx.android.synthetic.main.activity_main.*
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
                for(i in 1..5){
                    var musicData= Music_Data_Entity().apply {
                        this.music_data= Music_Data(i.toString(),i,i.toString(),true)
                    }
                    database.getMusic_Data_Dao().insert(musicData)
                }

                for(i in 1..5){
                    var musicList= Music_List_Entity().apply {
                        this.id=i.toLong()
                        this.child_tableName=i.toString()
                    }
                    database.getMusic_List_Dao().insert(musicList)
                }
            }


        }

        button5.setOnClickListener {



            AsyncTask.execute{
                database= MusicDatabase.getMusicDatabase(this@MainActivity)
                for(i in 1..3){
                    var musicTest= Music_ListData_Entity().apply {
                        // this.id=i.toLong()
                        this.childId=i.toLong()
                        this.childName=i.toString()
                    }
                    database.getMusic_Test_Dao().insert(musicTest)
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


