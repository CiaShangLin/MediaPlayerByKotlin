package com.shang.mediaplayerbykotlin

import android.util.Log
import androidx.lifecycle.LiveData

class MediaLiveData : LiveData<Int>() {
    override fun onActive() {
        super.onActive()
        Log.d("TAG","onActive")
    }

    override fun onInactive() {
        super.onInactive()
        Log.d("TAG","onInactive")
    }
}