package com.shang.mediaplayerbykotlin

import android.app.AlertDialog
import android.arch.persistence.room.Database
import android.content.Context
import android.content.DialogInterface
import android.opengl.Visibility
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_ListName_Entity
import kotlinx.android.synthetic.main.input_edittext.view.*
import kotlinx.android.synthetic.main.play_list_layout.view.*
import org.jetbrains.anko.runOnUiThread

/**
 * Created by Shang on 2018/8/23.
 */
class PlayListAdapter(var context: Context, var playList: MutableList<Music_ListName_Entity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var database: MusicDatabase

    init {
        database = MusicDatabase.getMusicDatabase(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.play_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return (playList.size+1)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (playList.size  == position) {
            holder.itemView.playListTv.visibility = View.INVISIBLE
            holder.itemView.playListBt.visibility = View.INVISIBLE
            holder.itemView.playListAddBt.visibility = View.VISIBLE

            holder.itemView.playListAddBt.setOnClickListener {

                var view=LayoutInflater.from(context).inflate(R.layout.input_edittext,null)
                AlertDialog.Builder(context)
                        .setTitle("輸入播放清單名稱")
                        .setView(view)
                        .setPositiveButton("新增", DialogInterface.OnClickListener { dialog, which ->
                            AsyncTask.execute {
                                var name=view.playListEt.text.toString().trim()
                                if(name.length!=0){
                                    AsyncTask.execute {
                                        var entity=Music_ListName_Entity().apply {
                                            this.tableName=name
                                        }

                                        database.getMusic_ListName_Dao().insert(entity)
                                        playList.add(entity)
                                    }
                                    context.runOnUiThread {
                                        Toast.makeText(context,"新增成功",Toast.LENGTH_SHORT).show()
                                        notifyDataSetChanged()
                                    }
                                }else{
                                    context.runOnUiThread {
                                        Toast.makeText(context,"名稱不能為空",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }).setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->

                        }).show()
            }

        } else {
            holder.itemView.playListTv.visibility = View.VISIBLE
            holder.itemView.playListBt.visibility = View.VISIBLE
            holder.itemView.playListAddBt.visibility = View.INVISIBLE

            holder.itemView.playListTv.text=playList.get(position).tableName
            holder.itemView.playListBt.setOnClickListener{
                var popupMenu= PopupMenu(context,it)
                popupMenu.menuInflater.inflate(R.menu.play_list_more_menu,popupMenu.menu)

                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.playListDelete->{
                            AsyncTask.execute {
                                database.getMusic_ListName_Dao().delete(Music_ListName_Entity().apply {
                                    this.id=playList.get(position).id
                                    this.tableName=playList.get(position).tableName
                                })
                                playList.removeAt(position)
                                context.runOnUiThread {
                                    notifyDataSetChanged()
                                    Toast.makeText(context,"刪除成功",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        R.id.playListUpdate->{
                            var view=LayoutInflater.from(context).inflate(R.layout.input_edittext,null)
                            view.playListEt.setText(playList.get(position).tableName.toString())
                            AlertDialog.Builder(context)
                                    .setView(view)
                                    .setPositiveButton("修改", DialogInterface.OnClickListener { dialog, which ->
                                        AsyncTask.execute {
                                            playList.get(position).tableName=view.playListEt.text.toString().trim()
                                            //database.getMusic_ListName_Dao().update()
                                            context.runOnUiThread {
                                                notifyDataSetChanged()
                                            }
                                        }




                                    }).setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->

                                    })
                                    .show()

                        }
                    }
                    true
                }
                popupMenu.show()
            }


        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}
}