package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.*
import android.support.annotation.NonNull
import org.jetbrains.annotations.Nullable


/*,foreignKeys = arrayOf(ForeignKey(entity = Music_Data_Entity::class,parentColumns = arrayOf("id")
,childColumns = arrayOf("list"),
onUpdate = ForeignKey.CASCADE,
onDelete = ForeignKey.CASCADE))*/
@Entity(tableName = Music_List_Entity.TABLE_NAME
)
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