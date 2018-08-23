package com.shang.mediaplayerbykotlin

import android.app.AlertDialog
import android.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import kotlinx.android.synthetic.main.music_item.view.*

/**
 * Created by Shang on 2018/8/21.
 */
class MusicAdapter(var context: Context, var musicList: MutableList<Music_Data_Entity>) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    companion object {
        val DATABASE_SUCCCESS = 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(position<musicList.size)
        {
            holder.itemView.title.text = musicList.get(position).name

            holder.itemView.time.text = FileUnits.lastModifiedToSimpleDateFormat(musicList.get(position).duration)

            holder.itemView.moreBt.setOnClickListener {
                var popupMenu=PopupMenu(context,it)
                popupMenu.menuInflater.inflate(R.menu.more_menu,popupMenu.menu)

                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.more_play->{}
                        R.id.more_add->{}
                    }
                    true
                }
                popupMenu.show()

            }

            holder.itemView.setOnClickListener {
                Toast.makeText(context, holder.itemView.getTag().toString(), Toast.LENGTH_SHORT).show()
            }

            holder.itemView.setTag(position)
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MusicAdapter.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.music_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

}