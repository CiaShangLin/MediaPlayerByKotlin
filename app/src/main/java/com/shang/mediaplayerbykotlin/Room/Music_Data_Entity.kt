package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Shang on 2018/8/14.
 */
@Entity(tableName = Music_Data_Entity.TABLE_NAME)
class Music_Data_Entity {

    companion object {
        const val TABLE_NAME = "Music_Data_Table"
    }


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Long = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "time")
    var time: Int = 0

    @ColumnInfo(name = "path")
    var path: String = ""


}