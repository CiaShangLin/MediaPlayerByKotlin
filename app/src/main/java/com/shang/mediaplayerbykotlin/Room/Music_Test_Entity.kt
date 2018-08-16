package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.*

/**
 * Created by Shang on 2018/8/16.
 */
@Entity(tableName = Music_Test_Entity.TABLE_NAME,
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


class Music_Test_Entity {

    companion object {
        const val TABLE_NAME="Music_Test"
    }


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Long=0


    var childId:Long=0


    var childName:String=""



}