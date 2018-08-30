package com.shang.mediaplayerbykotlin.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.shang.mediaplayerbykotlin.FileUnits
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.R
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import com.shang.mediaplayerbykotlin.Room.Music_ListData_Entity
import com.shang.mediaplayerbykotlin.Room.Music_ListName_Entity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Shang on 2018/8/21.
 */
class MusicDataAdapter(var context: Context, var musicList: MutableList<Music_Data_Entity>) : RecyclerView.Adapter<MusicDataAdapter.ViewHolder>() {


    lateinit var database: MusicDatabase

    init {
        database = MusicDatabase.getMusicDatabase(context)
    }

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
                    R.id.more_add -> {
                        doAsync {
                            var playListName = database.getMusic_ListName_Dao().getAll()
                            var array = database.getMusic_ListName_Dao().getAllTableName()
                            uiThread {
                                addDialog(array, playListName,position)
                            }
                        }
                    }
                }
                true
            }
            popupMenu.show()
        }

        holder.cardview.setOnClickListener {
            context.startActivity(Intent(context, PlayMusicActivity::class.java).apply {
                this.putExtra(MPC_Interface.INDEX, position)
            })
        }

        holder.itemView.setTag(position)

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MusicDataAdapter.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.music_data_item, parent, false)

        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return musicList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findViewById<TextView>(R.id.title)
        val time = view.findViewById<TextView>(R.id.time)
        val moreBt = view.findViewById<ImageButton>(R.id.moreBt)
        val cardview = view.findViewById<CardView>(R.id.cardview)

    }

    fun addDialog(array: Array<String>, playListName: MutableList<Music_ListName_Entity>,position:Int) {
        AlertDialog.Builder(context,android.R.style.Theme_Material_Light_Dialog)
                .setTitle("加入至播放清單")
                .setItems(array, DialogInterface.OnClickListener { dialog, which ->
                    doAsync {
                        Log.d("Music",playListName.get(which).id.toString()+" "+musicList.get(position).path)

                        database.getMusic_ListData_Dao().insert(Music_ListData_Entity().apply {
                            this.musicPath=musicList.get(position).path
                            this.table_id=playListName.get(which).id
                        })
                        uiThread {
                            Toast.makeText(context,"新增至"+array[which],Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .show()
    }

}