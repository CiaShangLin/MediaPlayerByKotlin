package com.shang.mediaplayerbykotlin.Dialog


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService
import com.shang.mediaplayerbykotlin.R
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

/**
 * Created by SERS on 2018/8/29.
 */
//睡眠定時器
class TimerDialog : DialogFragment() {

    companion object {
        const val TAG = "TimerDialog"

        private var timerDialog: TimerDialog? = null
        private var compositeDisposable = CompositeDisposable()
        fun getInstance(): TimerDialog? {
            if (timerDialog == null) {
                timerDialog = TimerDialog()
            }
            return timerDialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        var view = inflater?.inflate(R.layout.timer_dialog, container)
        var timerHoursEt = view!!.findViewById<EditText>(R.id.timerHoursEt)
        var timerMinuteEt = view!!.findViewById<EditText>(R.id.timerMinuteEt)
        var timerSecondEt = view!!.findViewById<EditText>(R.id.timerSecondEt)
        var timerStartBt = view!!.findViewById<Button>(R.id.timerStartBt)
        var timerResetBt = view!!.findViewById<Button>(R.id.timerResetBt)
        var imgClose = view!!.findViewById<ImageView>(R.id.imgClose)



        timerStartBt.setOnClickListener {
            try {
                var hours = if (timerHoursEt.text.isEmpty()) 0 else timerHoursEt.text.toString().toInt() * 60 * 60
                var minute = if (timerMinuteEt.text.isEmpty()) 0 else timerMinuteEt.text.toString().toInt() * 60
                var second = if (timerSecondEt.text.isEmpty()) 0 else timerSecondEt.text.toString().toInt()
                var timer = (hours + minute + second) * 1000

                var disposable = Observable.timer(timer.toLong(), TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer {
                            MPC?.mpc_mode?.pause()
                            Log.d(TAG, "onNext")
                        }, Consumer {
                            Log.d(TAG, it.message.toString())
                        }, Action {
                            Log.d(TAG, "onComplete")
                        }, Consumer {
                            context?.toast("設置完成")
                            dismiss()
                            Log.d(TAG, "onSubscribe")
                        })

                compositeDisposable.add(disposable)

            } catch (e: Exception) {
                context?.toast("請輸入有效時間")
            }
        }

        timerResetBt.setOnClickListener {
            compositeDisposable.dispose()
            context?.toast("重置完成")
            dismiss()
        }

        imgClose.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        dialog!!.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}