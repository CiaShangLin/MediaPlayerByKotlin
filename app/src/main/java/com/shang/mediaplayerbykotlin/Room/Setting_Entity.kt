package com.shang.mediaplayerbykotlin.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by SERS on 2018/8/23.
 */

@Entity(tableName = Setting_Entity.TABLE_NAME)
class Setting_Entity {

    companion object {
        const val TABLE_NAME: String = "Setting_Table"
        val key: String = "Setting"

        fun getSortType(sort_type:Int):String{
            return when(sort_type){
                1->{Music_Data_Entity.MODIFIED}
                2->{Music_Data_Entity.NAME}
                3->{Music_Data_Entity.DURATION}
                else->{Music_Data_Entity.MODIFIED}
            }
        }
    }


    @PrimaryKey
    var name: String = key

    // true=升序 false=降序
    var sort_mode: Boolean = true

    //1=修改日期 2=名稱長度 3=時間長度
    var sort_type: Int = 1



}