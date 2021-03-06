package com.shang.mediaplayerbykotlin.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.shang.mediaplayerbykotlin.Activity.PlayListActivity
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.FileUnits
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.R
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.Music_ListData_Entity

/**
 * Created by Shang on 2018/8/26.
 */
class PlayListDataAdapter(var context: Context, var musicList: MutableList<Music_Data_Entity>, var playListName_id: Long) : RecyclerView.Adapter<PlayListDataAdapter.ViewHolder>() {

    val database: MusicDatabase by lazy {
        MusicDatabase.getMusicDatabase(context)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.play_list_data_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataTitle.text = musicList.get(position).name
        holder.dataTime.text = FileUnits.lastModifiedToSimpleDateFormat(musicList.get(position).duration)
        holder.dataCardview.setOnClickListener {
            Log.d("PlayListDataAdapter", position.toString() + " " + musicList.get(position).name)
            context.startActivity(Intent(context, PlayMusicActivity::class.java).apply {
                this.putExtra(MPC_Interface.INDEX, position)
            })
        }
        holder.dataMore.setOnClickListener {
            popupMenu(it, position)
        }

    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dataTitle = view.findViewById<TextView>(R.id.dataTitle)
        val dataTime = view.findViewById<TextView>(R.id.dataTime)
        val dataIg = view.findViewById<ImageView>(R.id.dataIg)
        val dataMore = view.findViewById<ImageButton>(R.id.dataMore)
        val dataCardview = view.findViewById<CardView>(R.id.dataCardview)
    }

    fun popupMenu(view: View, position: Int) {
        var popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.play_list_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.playListData_delete -> {
                    database.getMusic_ListData_Dao().delete(Music_ListData_Entity().apply {
                        this.table_id = playListName_id
                        this.musicPath = musicList.get(position).path
                    })

                    musicList.removeAt(position)

                    notifyDataSetChanged()
                }
            }
            true
        }
        popupMenu.show()
    }
}