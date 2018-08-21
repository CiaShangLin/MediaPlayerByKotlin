package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.File

/**
 * Created by Shang on 2018/8/14.
 */
@Entity(tableName = Music_Data_Entity.TABLE_NAME)
class Music_Data_Entity {

    companion object {
        const val TABLE_NAME = "Music_Data_Table"
    }

    @PrimaryKey()
    var name:String=""

    var duration:Long=0
    var path:String=""
    var favorite:Boolean=false
    var modified:Long=0
    var picture:String=""

    //如果有原有資料庫 新增欄位會報錯 要不刪除資料庫,不然就是用程式碼解決
}
