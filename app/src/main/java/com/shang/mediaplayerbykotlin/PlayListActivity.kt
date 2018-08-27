package com.shang.mediaplayerbykotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.shang.mediaplayerbykotlin.Adapter.PlayListDataAdapter
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MPC_normal

import kotlinx.android.synthetic.main.activity_play_list.*

class PlayListActivity : AppCompatActivity() {

    val TAG = "PlayListActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)

        MPC.musicList.forEach {
            Log.d(TAG, it.name + " " + it.path + " " + it.modified)
        }

        var adapter = PlayListDataAdapter(this, MPC.musicList)
        play_list_recyc.layoutManager = LinearLayoutManager(this)
        play_list_recyc.adapter = adapter


        var item = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback((ItemTouchHelper.UP or ItemTouchHelper.DOWN), 0) {

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                var from = viewHolder!!.adapterPosition
                val to = target!!.adapterPosition

                var data=adapter.musicList.removeAt(from)
                adapter.musicList.add(to,data)
                adapter.notifyItemMoved(from,to)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        item.attachToRecyclerView(play_list_recyc)
        play_list_recyc.addItemDecoration(item)

    }

}
