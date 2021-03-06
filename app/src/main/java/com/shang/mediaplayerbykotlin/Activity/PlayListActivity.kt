package com.shang.mediaplayerbykotlin.Activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shang.mediaplayerbykotlin.Adapter.PlayListDataAdapter
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.R
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.Music_ListData_Entity
import kotlinx.android.synthetic.main.activity_play_list.*
import java.util.*

class PlayListActivity : AppCompatActivity() {

    val TAG = "PlayListActivity"

    val database: MusicDatabase by lazy {
        MusicDatabase.getMusicDatabase(this)
    }


    private var dataList: MutableList<Music_ListData_Entity> = mutableListOf()

    private val playListName_id by lazy {
        intent.getLongExtra(MPC_Interface.ID, 0)
    }
    private val toolbarTitle by lazy {
        intent.getStringExtra(MPC_Interface.NAME)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)

        changeData()

    }

    private fun changeData() {

        dataList = database.getMusic_ListData_Dao().getListDataFromListName(playListName_id)

        var musicList = mutableListOf<Music_Data_Entity>()
        dataList.forEach {
            musicList.add(database.getMusic_Data_Dao().findListData(it.musicPath))
        }
        MPC.musicList = musicList

        initView()
    }

    private fun initView() {

        setSupportActionBar(play_list_toolbar)
        play_list_toolbar.title = toolbarTitle
        play_list_toolbar.setNavigationIcon(R.drawable.ic_back)
        play_list_toolbar.setNavigationOnClickListener {
            finish()
        }

        var adapter = PlayListDataAdapter(this, MPC.musicList, playListName_id)
        play_list_recyc.layoutManager = LinearLayoutManager(this)
        play_list_recyc.adapter = adapter


        setCollapsingBackground()


        var item = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback((ItemTouchHelper.UP or ItemTouchHelper.DOWN), 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                var from = viewHolder!!.adapterPosition
                val to = target!!.adapterPosition

                if (from < to) {

                    for (i in from..to - 1) {
                        Collections.swap(adapter.musicList, i, i + 1)

                    }
                } else {
                    for (i in from downTo to + 1) {
                        Collections.swap(adapter.musicList, i, i - 1)
                    }
                }
                var data = adapter.musicList.removeAt(from)
                adapter.musicList.add(to, data)
                adapter.notifyItemMoved(from, to)



                update(from, to)
                MPC.musicList = adapter.musicList

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })

        item.attachToRecyclerView(play_list_recyc)
        play_list_recyc.addItemDecoration(item)
    }


    fun update(from: Int, to: Int) {
        try{
            var temp = dataList.removeAt(from)
            dataList.add(to, temp)

            for (i in dataList.indices) {
                database.getMusic_ListData_Dao().update(dataList.get(i).apply {
                    this.position = i
                })
            }

            dataList.forEach {
                Log.d(TAG, "dataList:  " + it.musicPath + " " + it.position)
            }
        }catch (e:Exception){}

    }

    fun setCollapsingBackground() {

        var list = mutableListOf<Bitmap>()
        MPC.musicList.forEach {
            var bitmap = BitmapFactory.decodeFile(it.picture)
            if (bitmap != null)
                list.add(bitmap)
        }

        if (list.size == 0) {
            playListImg.background = resources.getDrawable(R.color.colorP)
        } else {
            var random = (Math.random() * list.size).toInt()
            playListImg.background = BitmapDrawable(list.get(random))
        }

    }


}
