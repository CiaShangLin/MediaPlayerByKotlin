package com.shang.mediaplayerbykotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import kotlinx.android.synthetic.main.music_item.view.*

/**
 * Created by Shang on 2018/8/21.
 */
class MusicAdapter(var context:Context,var musicList : MutableList<Music_Data_Entity>): RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    companion object {
        val DATABASE_SUCCCESS=1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.title.text=musicList.get(position).name

        holder.itemView.time.text=FileUnits.lastModifiedToSimpleDateFormat(musicList.get(position).duration)

        holder.itemView.moreBt.setOnClickListener{
            Toast.makeText(context,musicList.get(position).name,Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setTag(position)

    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MusicAdapter.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.music_item,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }



    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    }

}