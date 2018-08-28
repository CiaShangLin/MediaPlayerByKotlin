package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.*
import org.jetbrains.annotations.NotNull

/**
 * Created by Shang on 2018/8/16.
 */
@Entity(tableName = Music_ListData_Entity.TABLE_NAME,primaryKeys = ["musicPath","table_id"],
        foreignKeys = arrayOf(ForeignKey(entity = Music_Data_Entity::class,
                parentColumns = arrayOf("path"),
                childColumns = arrayOf("musicPath"),
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
                ForeignKey(entity = Music_ListName_Entity::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("table_id"),
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE))
)
//ForeignKey.CASCADE=會自動更新
//如果要定義2個以上的primaryKey,要使用primaryKeys
//由於是外來建 建議不要設置欄位名稱
class Music_ListData_Entity {

    companion object {
        const val TABLE_NAME="Music_ListData_Table"
    }

    @ColumnInfo(name="musicPath")
    var musicPath:String=""

    @ColumnInfo(name="table_id")
    var table_id:Long=0

    @ColumnInfo(name="position")
    var position:Int=0
}