package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert

@Dao
interface Music_Test_Dao {


    @Insert
    fun insert(musicTest:Music_Test_Entity)

}