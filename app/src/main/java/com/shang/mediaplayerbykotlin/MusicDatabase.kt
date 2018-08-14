package com.shang.mediaplayerbykotlin

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(entities =[(MusicData::class)],version = 1)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun getMusicDao():MusicDao
}