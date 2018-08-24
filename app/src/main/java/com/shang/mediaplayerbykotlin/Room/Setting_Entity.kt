package com.shang.mediaplayerbykotlin.Room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by SERS on 2018/8/23.
 */

@Entity(tableName = Setting_Entity.TABLE_NAME)
class Setting_Entity  {

    companion object {
        const val TABLE_NAME:String="Setting_Table"
        val key:String="Setting"
    }


    @PrimaryKey
    var name:String=key

    // true=升序 false=降序
    var sort_mode:Boolean=true

    //0=修改日期 1=名稱長度 2=時間長度
    var sort_type:Int=1


}