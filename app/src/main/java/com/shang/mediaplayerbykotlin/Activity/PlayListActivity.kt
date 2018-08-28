package com.shang.mediaplayerbykotlin.Activity

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.shang.mediaplayerbykotlin.Adapter.PlayListDataAdapter
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.R
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.Music_ListData_Entity

import kotlinx.android.synthetic.main.activity_play_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PlayListActivity : AppCompatActivity() {

    val TAG = "PlayListActivity"

    val database: MusicDatabase by lazy {
        //只會調用一次 用於val
        MusicDatabase.getMusicDatabase(this)
    }


    lateinit var DataList: MutableList<Music_ListData_Entity>
    var playListName_id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)

        changeData()

    }

    fun initView() {
        var adapter = PlayListDataAdapter(this, MPC.musicList)
        play_list_recyc.layoutManager = LinearLayoutManager(this)
        play_list_recyc.adapter = adapter


        var item = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback((ItemTouchHelper.UP or ItemTouchHelper.DOWN), 0) {

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                var from = viewHolder!!.adapterPosition
                val to = target!!.adapterPosition

                var data = adapter.musicList.removeAt(from)
                adapter.musicList.add(to, data)
                adapter.notifyItemMoved(from, to)
                MPC.musicList = adapter.musicList

                AsyncTask.execute {
                    update(from, to)
                }

                MPC.musicList.forEach {
                    Log.d(TAG, "MPC:" + it.name)
                }

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        item.attachToRecyclerView(play_list_recyc)
        play_list_recyc.addItemDecoration(item)
    }

    fun changeData() {
        doAsync {
            playListName_id = intent.getLongExtra("ID", 0)
            DataList = database.getMusic_ListData_Dao().getListDataFromListName(playListName_id)
            update(0, 0)

            var musicList = mutableListOf<Music_Data_Entity>()
            DataList.forEach {
                musicList.add(database.getMusic_Data_Dao().findListData(it.musicPath))
            }
            MPC.musicList = musicList

            uiThread {
                initView()
            }
        }
    }

    fun update(from: Int, to: Int) {
        var temp = DataList.removeAt(from)
        DataList.add(to, temp)

        for (i in DataList.indices) {
            database.getMusic_ListData_Dao().update(DataList.get(i).apply {
                this.position = i
            })
        }

        DataList.forEach {
            Log.d(TAG, "DataList:  " + it.musicPath + " " + it.position)
        }


    }

}
