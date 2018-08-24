package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = Music_ListName_Entity.TABLE_NAME)
class Music_ListName_Entity {

    companion object {
        const val TABLE_NAME="Music_ListName_Table"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Long=0


    @ColumnInfo(name="tableName")
    var tableName:String=""
}