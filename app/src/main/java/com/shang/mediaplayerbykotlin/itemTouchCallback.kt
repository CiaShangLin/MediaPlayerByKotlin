package com.shang.mediaplayerbykotlin

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.shang.mediaplayerbykotlin.Adapter.PlayListDataAdapter
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.text.FieldPosition

/**
 * Created by SERS on 2018/8/28.
 */
class itemTouchCallback(var musicList:MutableList<Music_Data_Entity>) : ItemTouchHelper.Callback(){


    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        var move=ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return ItemTouchHelper.Callback.makeMovementFlags(move,0)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        if(viewHolder!=null && target!=null){
            val fromPos=viewHolder.adapterPosition
            val toPos=target.adapterPosition


            true
        }
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }
}