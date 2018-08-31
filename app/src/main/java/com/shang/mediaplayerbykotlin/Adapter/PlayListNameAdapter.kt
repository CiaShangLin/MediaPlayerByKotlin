package com.shang.mediaplayerbykotlin.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.shang.mediaplayerbykotlin.Activity.PlayListActivity
import com.shang.mediaplayerbykotlin.MP.MPC_Interface
import com.shang.mediaplayerbykotlin.R
import com.shang.mediaplayerbykotlin.Room.MusicDatabase
import com.shang.mediaplayerbykotlin.Room.Music_ListName_Entity
import kotlinx.android.synthetic.main.input_edittext.view.*
import kotlinx.android.synthetic.main.play_list_name_item.view.*
import org.jetbrains.anko.*

/**
 * Created by Shang on 2018/8/23.
 */
class PlayListNameAdapter(var context: Context, var playList: MutableList<Music_ListName_Entity>) :
        RecyclerView.Adapter<PlayListNameAdapter.ViewHolder>() {


    lateinit var database: MusicDatabase

    init {
        database = MusicDatabase.getMusicDatabase(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlayListNameAdapter.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.play_list_name_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return (playList.size + 1)
    }

    override fun onBindViewHolder(holder: PlayListNameAdapter.ViewHolder, position: Int) {

        if (playList.size == position) {
            holder.playListTv.visibility = View.INVISIBLE
            holder.playListBt.visibility = View.INVISIBLE
            holder.playListAddBt.visibility = View.VISIBLE
            holder.playListCon.setBackgroundColor(context.resources.getColor(R.color.tra))

            holder.playListAddBt.setOnClickListener {
                addPlayListDialog()
            }

        } else {
            holder.playListTv.visibility = View.VISIBLE
            holder.playListBt.visibility = View.VISIBLE
            holder.playListAddBt.visibility = View.INVISIBLE
            holder.playListCon.setBackgroundColor(context.resources.getColor(R.color.colorAcc))


            holder.playListTv.text = playList.get(position).tableName
            holder.playListBt.setOnClickListener {
                popupMenu(it, position)
            }

            holder.playListCardView.setOnClickListener {
                context.startActivity(Intent(context, PlayListActivity::class.java).apply {
                    this.putExtra(MPC_Interface.ID, playList.get(position).id)
                    this.putExtra(MPC_Interface.NAME,playList.get(position).tableName)
                })
            }

        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val playListTv = view.findViewById<TextView>(R.id.playListTv)

        val playListBt = view.findViewById<ImageButton>(R.id.playListBt)

        val playListAddBt = view.findViewById<ImageButton>(R.id.playListAddBt)

        val playListCardView = view.findViewById<CardView>(R.id.playListCardView)

        val playListCon=view.findViewById<ConstraintLayout>(R.id.playListCon)

    }

    fun popupMenu(it: View, position: Int) {
        var popupMenu = PopupMenu(context, it)
        popupMenu.menuInflater.inflate(R.menu.play_list_more_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.playListDelete -> {
                    doAsync {
                        database.getMusic_ListName_Dao().delete(Music_ListName_Entity().apply {
                            this.id = playList.get(position).id
                            this.tableName = playList.get(position).tableName
                        })
                        playList.removeAt(position)
                        uiThread {
                            notifyDataSetChanged()
                            Toast.makeText(context, "刪除成功", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                R.id.playListUpdate -> {
                    var view = LayoutInflater.from(context).inflate(R.layout.input_edittext, null)
                    view.playListEt.setText(playList.get(position).tableName)   //顯示現在名稱
                    AlertDialog.Builder(context)
                            .setView(view)
                            .setPositiveButton("修改", DialogInterface.OnClickListener { dialog, which ->
                                doAsync {
                                    playList.get(position).tableName = view.playListEt.text.toString().trim()
                                    database.getMusic_ListName_Dao().update(Music_ListName_Entity().apply {
                                        this.id = playList.get(position).id
                                        this.tableName = view.playListEt.text.toString().trim()
                                    })
                                    uiThread {
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

    fun addPlayListDialog() {

        var view = LayoutInflater.from(context).inflate(R.layout.input_edittext, null)
        AlertDialog.Builder(context)
                .setTitle("輸入播放清單名稱")
                .setView(view)
                .setPositiveButton("新增", DialogInterface.OnClickListener { dialog, which ->

                    var name = view.playListEt.text.toString().trim()
                    if (name.length != 0) {
                        doAsync {
                            var entity = Music_ListName_Entity().apply {
                                this.tableName = name
                            }

                            playList.add(entity)
                            database.getMusic_ListName_Dao().insert(entity)


                            uiThread {
                                Toast.makeText(context, "新增成功", Toast.LENGTH_SHORT).show()
                                notifyDataSetChanged()
                            }
                        }
                    } else {
                        context.runOnUiThread {
                            Toast.makeText(context, "名稱不能為空", Toast.LENGTH_SHORT).show()
                        }
                    }

                }).setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->

                }).show()
    }
}