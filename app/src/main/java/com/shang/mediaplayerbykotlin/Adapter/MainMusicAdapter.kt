package com.shang.mediaplayerbykotlin.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import com.shang.mediaplayerbykotlin.FileUnits
import com.shang.mediaplayerbykotlin.PlayMusicActivity
import com.shang.mediaplayerbykotlin.R
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import kotlinx.android.synthetic.main.music_item.view.*

/**
 * Created by Shang on 2018/8/21.
 */
class MainMusicAdapter(var context: Context, var musicList: MutableList<Music_Data_Entity>) : RecyclerView.Adapter<MainMusicAdapter.ViewHolder>() {

    companion object {
        val DATABASE_SUCCCESS = 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.title.text = musicList.get(position).name

        holder.time.text = FileUnits.lastModifiedToSimpleDateFormat(musicList.get(position).duration)

        holder.moreBt.setOnClickListener {
            var popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.more_play -> {
                    }
                    R.id.more_add -> {
                    }
                }
                true
            }
            popupMenu.show()
        }

        holder.cardview.setOnClickListener {
            context.startActivity(Intent(context, PlayMusicActivity::class.java).apply {
                this.putExtra("index", position)
            })
        }

        holder.itemView.setTag(position)


    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.music_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title=view.findViewById<TextView>(R.id.title)
        val time=view.findViewById<TextView>(R.id.time)
        val moreBt=view.findViewById<ImageButton>(R.id.moreBt)
        val cardview=view.findViewById<CardView>(R.id.cardview)

    }

}