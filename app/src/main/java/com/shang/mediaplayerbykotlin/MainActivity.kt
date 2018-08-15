package com.shang.mediaplayerbykotlin

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_List_Entity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {


    val TAG = "Music"
    lateinit var mediaPlayer: MediaPlayer

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

            var database= MusicDatabase.getMusicDatabase(this@MainActivity)


            AsyncTask.execute{

                for(i in 0..5){
                    var musicData= Music_Data_Entity()
                    musicData.name=i.toString()+" name"
                    musicData.path=i.toString()+" path"
                    //musicData.time=MediaPlayer.create(this@MainActivity, Uri.fromFile(file.get(i))).duration
                    database.getMusicDao().insert(musicData)
                }


                for(item in  database.getMusicDao().getAll()){
                    Log.d("TAG","${item.id} ${item.name} ${item.path} ${item.time}")
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


