package com.shang.mediaplayerbykotlin.Room

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Shang on 2018/9/5.
 */
class MusicDatabaseTest {
    private var db: MusicDatabase? = null

    @Test
    fun createDB(){
        var context=InstrumentationRegistry.getTargetContext()
        db=Room.inMemoryDatabaseBuilder(context,MusicDatabase::class.java).build()
    }

    @Test
    fun closeDB(){
        db!!.close()
    }

    @Test
    fun insert(){
        var musicDao=db!!.getMusic_Data_Dao()
        var music_entity=Music_Data_Entity().apply {
            this.path="path"
            this.name="name"
        }
        musicDao.insert(music_entity)
    }


}