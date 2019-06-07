package com.shang.mediaplayerbykotlin

import android.content.Context
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu

class MyPopupMenu: PopupMenu,PopupMenu.OnMenuItemClickListener {

    constructor(context: Context, view: View,menuRes:Int):super(context,view){
        super.inflate(menuRes)
    }


    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        return true
    }



}