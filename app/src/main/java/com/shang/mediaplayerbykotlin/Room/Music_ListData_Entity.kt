package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.*

/**
 * Created by Shang on 2018/8/16.
 */
@Entity(tableName = Music_ListData_Entity.TABLE_NAME,
        foreignKeys = arrayOf(ForeignKey(entity = Music_Data_Entity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("childId"),
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
                ForeignKey(entity = Music_List_Entity::class,
                        parentColumns = arrayOf("child_tableName"),
                        childColumns = arrayOf("childName"),
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE))
)


class Music_ListData_Entity {

    companion object {
        const val TABLE_NAME="Music_Test"
    }


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Long=0



    //由於是外來建 建議不要設置欄位名稱
    var childId:Long=0
    var childName:String=""



}