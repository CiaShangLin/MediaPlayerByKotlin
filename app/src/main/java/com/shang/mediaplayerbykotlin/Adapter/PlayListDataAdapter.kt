package com.shang.mediaplayerbykotlin.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.shang.mediaplayerbykotlin.FileUnits
import com.shang.mediaplayerbykotlin.R
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import kotlinx.android.synthetic.main.play_list_data_item.view.*
import org.jetbrains.anko.find

/**
 * Created by Shang on 2018/8/26.
 */
class PlayListDataAdapter(var context: Context, var musicList: MutableList<Music_Data_Entity>) : RecyclerView.Adapter<PlayListDataAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlayListDataAdapter.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.play_list_data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataTitle.text=musicList.get(position).name
        holder.dataTime.text= FileUnits.lastModifiedToSimpleDateFormat(musicList.get(position).duration)

    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dataTitle = view.findViewById<TextView>(R.id.dataTitle)
        val dataTime = view.findViewById<TextView>(R.id.dataTime)
        val dataIg = view.findViewById<ImageView>(R.id.dataIg)
        val dataMore=view.findViewById<ImageButton>(R.id.dataMore)
    }
}