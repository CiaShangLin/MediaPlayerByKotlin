package com.shang.mediaplayerbykotlin.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = arrayOf(Music_Data_Entity::class, Music_ListName_Entity::class, Music_ListData_Entity::class, Setting_Entity::class), version = 2)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun getMusic_Data_Dao(): Music_Data_Dao           //沒有新增DAO的話 entity會過不了
    abstract fun getMusic_ListName_Dao(): Music_ListName_Dao
    abstract fun getMusic_ListData_Dao(): Music_ListData_Dao
    abstract fun getSetting_Dao(): Setting_Dao

    companion object {
        val DATABASE_NAME = "MUSIC_DATABASE"
        var musicDatabase: MusicDatabase? = null

        fun getMusicDatabase(context: Context): MusicDatabase {
            if (musicDatabase == null) {
                musicDatabase = Room.databaseBuilder(context, MusicDatabase::class.java, DATABASE_NAME)
                        .allowMainThreadQueries()
                        .addMigrations(migration)
                        .build()
            }
            return musicDatabase as MusicDatabase
        }

        var migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${Music_Data_Entity.TABLE_NAME}" + " ADD COLUMN temp_delete INTEGER NOT NULL DEFAULT 0")
            }
        }

    }
}

/*
步驟
1.新增@Entity 一定要有@PremaryKey
2.新增Dao  是一個interface
3.到繼承RoomDatabase()的@Database(entities=arrayOf(新增class) )
4.新增abstract fun getSetting_Dao(): Setting_Dao
*/