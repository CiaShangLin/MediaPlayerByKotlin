package com.shang.mediaplayerbykotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaPlayerModel: ViewModel() {
    private var mediaStatus: MutableLiveData<Int> = MutableLiveData()
    var m = MediaLiveData()

}