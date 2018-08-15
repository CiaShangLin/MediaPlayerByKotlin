package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = Music_List_Entity.TABLE_NAME)
class Music_List_Entity {

    companion object {
        const val TABLE_NAME="Music_List_Table"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Long=0


    @ColumnInfo(name="child_tableName")
    var child_tableName:String=""



}