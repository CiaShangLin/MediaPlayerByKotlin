package com.shang.mediaplayerbykotlin

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Created by Shang on 2018/8/22.
 */
class MoreDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var array= arrayOf("播放","加入至播放清單")
        var dialog=AlertDialog.Builder(activity,android.R.style.TextAppearance_Widget_PopupMenu_Small).setItems(array, DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(activity,which.toString(),Toast.LENGTH_SHORT).show()
        }).create()

        return dialog
    }

}