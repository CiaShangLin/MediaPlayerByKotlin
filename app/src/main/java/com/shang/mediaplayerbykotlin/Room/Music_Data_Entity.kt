package com.shang.mediaplayerbykotlin.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Shang on 2018/8/14.
 */
@Entity(tableName = Music_Data_Entity.TABLE_NAME)
class Music_Data_Entity {

    companion object {
        const val TABLE_NAME = "Music_Data_Table"
        const val PATH: String = "path"
        const val NAME: String = "name"
        const val DURATION: String = "duration"
        const val FAVORITE: String = "favorite"
        const val MODIFIED: String = "modified"
        const val PICTURE: String = "picture"
    }

    @PrimaryKey()
    @ColumnInfo(name = PATH)
    var path: String = ""

    @ColumnInfo(name = NAME)
    var name: String = ""

    @ColumnInfo(name = DURATION)
    var duration: Long = 0

    @ColumnInfo(name = FAVORITE)
    var favorite: Boolean = false

    @ColumnInfo(name = MODIFIED)
    var modified: Long = 0

    @ColumnInfo(name = PICTURE)
    var picture: String = ""

    //如果有原有資料庫 新增欄位會報錯 要不刪除資料庫,不然就是用程式碼解決
}
