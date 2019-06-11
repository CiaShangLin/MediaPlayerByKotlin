package com.shang.mediaplayerbykotlin

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.shang.mediaplayerbykotlin.Room.Setting_Entity
import com.shang.mediaplayerbykotlin.ViewModel.MediaPlayerModel

class MyPopupMenu : PopupMenu, PopupMenu.OnMenuItemClickListener {

    private val TAG:String="MyPopupMenu"
    private lateinit var mMediaPlayerModel: MediaPlayerModel
    private var settingEntity: Setting_Entity? = null

    constructor(context: Context, view: View, menuRes: Int, mediaPlayerModel: MediaPlayerModel) : super(context, view) {
        super.inflate(menuRes)
        this.mMediaPlayerModel = mediaPlayerModel
        this.settingEntity = mMediaPlayerModel.getSettingNow()

        //初始化
        Log.v(TAG,"mode:${settingEntity?.sort_mode} type:${settingEntity?.sort_type}" )
        if (settingEntity != null) {
            menu.findItem(R.id.sort_mode).setChecked(settingEntity?.sort_mode!!)
            menu.getItem(settingEntity?.sort_type!!).setChecked(true)
        } else {
            mMediaPlayerModel.insertSetting(Setting_Entity())
        }
    }


    override fun onMenuItemClick(p0: MenuItem): Boolean {
        var setting_Entity = mMediaPlayerModel.getSettingNow()
        when (p0.itemId) {
            R.id.sort_mode -> {
                setting_Entity.sort_mode = !p0.isChecked
            }
            R.id.sort_modify -> {
                setting_Entity.sort_type = 1
            }
            R.id.sort_name -> {
                setting_Entity.sort_type = 2
            }
            R.id.sort_time -> {
                setting_Entity.sort_type = 3
            }
        }
        Log.v(TAG,"mode:${setting_Entity.sort_mode} type:${setting_Entity.sort_type}" )
        mMediaPlayerModel.updateSetting(setting_Entity) //更新
        return true
    }


}