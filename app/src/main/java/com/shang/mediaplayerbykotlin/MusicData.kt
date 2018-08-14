package com.shang.mediaplayerbykotlin

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Shang on 2018/8/14.
 */
@Entity(tableName = MusicData.TABLE_NAME)
class MusicData {

    companion object {
        const val TABLE_NAME = "MusicTable"
    }

    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "time")
    var time: Int = 0

    @ColumnInfo(name = "path")
    var path: String = ""


}