package com.shang.mediaplayerbykotlin.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.shang.mediaplayerbykotlin.R

/**
 * Created by SERS on 2018/8/31.
 */
class LoadDialog : androidx.fragment.app.DialogFragment() {

    companion object {
        val TAG = "LoadDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
        setStyle(STYLE_NORMAL, R.style.LoadingDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.loading_dialog,container,false)
    }

    override fun onResume() {
        super.onResume()

        dialog.window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }
}