package com.shang.mediaplayerbykotlin

import android.content.Context
import android.provider.MediaStore
import androidx.test.InstrumentationRegistry
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Shang on 2018/9/5.
 */
class FileUnitsTest{

    @Test
    fun musicNotNull() {
        val context = InstrumentationRegistry.getTargetContext()
        var entity = mutableListOf<Music_Data_Entity>()
        var uri = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        uri.moveToFirst()
        while (uri.moveToNext()) {
            var name = uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
            var duration = uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.DURATION))
            var path = uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.DATA))
            var modified = uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED))
            var picture = uri.getString(uri.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))

            entity.add(Music_Data_Entity().apply {
                this.name = name
                this.path = path
                this.duration = duration.toLong()
                this.modified = modified.toLong()
                this.favorite = false
                this.picture = picture
            })
        }
        assertNotNull(entity)

    }



}