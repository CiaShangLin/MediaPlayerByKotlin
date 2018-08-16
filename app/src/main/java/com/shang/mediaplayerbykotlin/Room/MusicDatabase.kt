package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(Music_Data_Entity::class, Music_ListName_Entity::class,Music_ListData_Entity::class), version = 1)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun getMusic_Data_Dao(): Music_Data_Dao           //沒有新增DAO的話 entity會過不了
    abstract fun getMusic_ListName_Dao(): Music_ListName_Dao
    abstract fun getMusic_ListData_Dao(): Music_ListData_Dao

    companion object {
        val DATABASE_NAME = "MUSIC_DATABASE"
        var musicDatabase: MusicDatabase? = null

        fun getMusicDatabase(context: Context): MusicDatabase {
            if (musicDatabase == null) {
                musicDatabase = Room.databaseBuilder(context, MusicDatabase::class.java, DATABASE_NAME).build()
            }
            return musicDatabase as MusicDatabase
        }

    }
}