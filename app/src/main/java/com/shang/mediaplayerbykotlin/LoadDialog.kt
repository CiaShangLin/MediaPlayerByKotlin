package com.shang.mediaplayerbykotlin

import android.app.ActionBar
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import org.jetbrains.anko.custom.style

/**
 * Created by SERS on 2018/8/31.
 */
class LoadDialog : DialogFragment() {

    companion object {
        val TAG = "LoadDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
        setStyle(STYLE_NO_TITLE or STYLE_NO_FRAME, R.style.LoadingDialog)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.loading_dialog, container)

        return view
    }


    override fun setStyle(style: Int, theme: Int) {
        super.setStyle(style, theme)
    }

    override fun onResume() {
        super.onResume()

        dialog.window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}