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

            AsyncTask.execute{
                database= MusicDatabase.getMusicDatabase(this@MainActivity)
                database.getMusic_Data_Dao().insert(Music_Data_Entity().apply {
                    this.music_data=Music_Data("1",1,"1",true)
                })

                database.getMusic_Data_Dao().insert(Music_Data_Entity().apply {
                    this.music_data=Music_Data("2",2,"2",true)
                })

                database.getMusic_Data_Dao().insert(Music_Data_Entity().apply {
                    this.music_data=Music_Data("3",3,"3",true)
                })

                database.getMusic_ListName_Dao().insert(Music_ListName_Entity().apply {
                    this.child_tableName="1"
                    this.id=1
                })

                database.getMusic_ListName_Dao().insert(Music_ListName_Entity().apply {
                    this.child_tableName="2"
                    this.id=2
                })
                database.getMusic_ListName_Dao().insert(Music_ListName_Entity().apply {
                    this.child_tableName="3"
                    this.id=3
                })

                database.getMusic_ListData_Dao().insert(Music_ListData_Entity().apply {
                    this.childId=1
                    this.childName="1"
                })

                database.getMusic_ListData_Dao().insert(Music_ListData_Entity().apply {
                    this.childId=2
                    this.childName="2"
                })

                database.getMusic_ListData_Dao().insert(Music_ListData_Entity().apply {
                    this.childId=3
                    this.childName="3"
                })
            }






        }

        button5.setOnClickListener {



            /*AsyncTask.execute{
                database= MusicDatabase.getMusicDatabase(this@MainActivity)
                database.getMusic_Data_Dao().delete(Music_Data_Entity().apply {
                    this.id=3
                })
            }*/

            AsyncTask.execute{
                database= MusicDatabase.getMusicDatabase(this@MainActivity)
                database.getMusic_ListName_Dao().delete(Music_ListName_Entity().apply {
                    this.child_tableName="2"
                    this.id=2
                })
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


