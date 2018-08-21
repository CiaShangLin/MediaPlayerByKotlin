package com.shang.mediaplayerbykotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity

/**
 * Created by Shang on 2018/8/21.
 */
class MusicAdapter(var context:Context,var musicList : MutableList<Music_Data_Entity>): RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
 
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MusicAdapter.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.cardview_ui,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }



    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }
}