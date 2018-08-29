package com.shang.mediaplayerbykotlin

import android.app.ActionBar
import android.app.DialogFragment
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.shang.mediaplayerbykotlin.Activity.MainActivity
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import kotlinx.android.synthetic.main.timer_dialog.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startService
import org.jetbrains.anko.toast

/**
 * Created by SERS on 2018/8/29.
 */
class TimerDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        var handler = Handler()
        var runnable = Runnable {
            MPC.mpc_mode.pause()
        }

        var view = inflater?.inflate(R.layout.timer_dialog, container)
        var timerHoursEt = view!!.findViewById<EditText>(R.id.timerHoursEt)
        var timerMinuteEt = view!!.findViewById<EditText>(R.id.timerMinuteEt)
        var timerSecondEt = view!!.findViewById<EditText>(R.id.timerSecondEt)
        var timerStartBt = view!!.findViewById<Button>(R.id.timerStartBt)
        var timerResetBt = view!!.findViewById<Button>(R.id.timerResetBt)

        timerStartBt.setOnClickListener {
            try {
                var hours = if (timerHoursEt.text.toString() == "") 0 else timerHoursEt.text.toString().toInt() * 60 * 60
                var minute = if (timerMinuteEt.text.toString() == "") 0 else timerMinuteEt.text.toString().toInt() * 60
                var second = if (timerSecondEt.text.toString() == "") 0 else timerSecondEt.text.toString().toInt()
                var timer = (hours + minute + second) * 1000

                handler.postDelayed(runnable, timer.toLong())
                toast("設置完成")
                dismiss()
            } catch (e: Exception) {
                toast("請輸入有效時間")
            }
        }

        timerResetBt.setOnClickListener {

            try {
                handler.removeCallbacks(runnable)
                toast("重置完成")
            } catch (e: Exception) {
                toast("發生錯誤")
            }
        }


        return view!!
    }

    override fun onResume() {
        super.onResume()

        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}