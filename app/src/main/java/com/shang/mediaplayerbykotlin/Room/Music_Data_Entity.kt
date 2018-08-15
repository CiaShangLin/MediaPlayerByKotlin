package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
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
    @ColumnInfo(name = "id")
    var id: Long = 0

    @Embedded
    lateinit var music_data:Music_Data

    //如果有原有資料庫 新增欄位會報錯 要不刪除資料庫,不然就是用程式碼解決
}

data class Music_Data(var name:String,var time:Int,var path:String,var favorite:Boolean){}