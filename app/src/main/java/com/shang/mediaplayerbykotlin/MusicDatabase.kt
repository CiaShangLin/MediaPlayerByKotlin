package com.shang.mediaplayerbykotlin

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities =[(MusicData::class)],version = 1)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun getMusicDao():MusicDao

    companion object {
        val DATABASE_NAME="MUSIC_DATABASE"
        var musicDatabase:MusicDatabase?=null

        fun getMusicDatabase(context:Context):MusicDatabase{
            if(musicDatabase==null){
                musicDatabase= Room.databaseBuilder(context,MusicDatabase::class.java, DATABASE_NAME).build()
            }
            return musicDatabase as MusicDatabase
        }

    }
}