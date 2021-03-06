package com.shang.mediaplayerbykotlin

import android.content.Context
import android.provider.MediaStore
import com.shang.mediaplayerbykotlin.Room.Music_Data_Entity
import java.io.File
import java.text.SimpleDateFormat

/**
 * Created by Shang on 2018/8/12.
 */
class FileUnits {

    companion object {

        val TAG = "FileUnits"
        var musicList = mutableListOf<File>()

        //最後修改時間轉成時間格式
        fun lastModifiedToSimpleDateFormat(time: Long): String {
            return SimpleDateFormat("mm:ss").format(time)
        }


        //取得所有Music底下的音樂檔
        fun findAllMusic(path: File) {
            try {
                var f = File(path.path)

                f.walkTopDown().filter {
                    it.isDirectory && (it.name in listOf<String>("Music", "music", "音樂"))
                }.forEach {
                    it.walk().filter {
                        it.isFile and (it.extension in listOf<String>("mp3", "flac", "3gp", "wav"))
                    }.forEach {
                        musicList.add(it)
                    }
                }
                findAllMusic(File(path.parent))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun findAllMusicFromContentResolver(context: Context): MutableList<Music_Data_Entity> {
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

            return entity
        }

        fun getPicture(entity: MutableList<Music_Data_Entity>, context: Context):MutableList<Music_Data_Entity>{
            var cursorAlbum = context.contentResolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART),
                    null, null, null)

            if(cursorAlbum!=null && cursorAlbum.count!=0){
                cursorAlbum.moveToFirst()
                do {
                    var index = cursorAlbum.getString(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID))
                    var str = cursorAlbum.getString(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
                    if (str != null){
                        for (it in entity.iterator()) {
                            if (it.picture == index) {
                                it.picture = str
                                break
                            }
                        }
                    }
                } while (cursorAlbum.moveToNext())
            }


            return entity
        }

        fun getOnePicture(picture:String, context: Context):String {
            var cursorAlbum = context.contentResolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART),
                    null, null, null)

            if(cursorAlbum!=null && cursorAlbum.count!=0)
            cursorAlbum.moveToFirst()
            do {
                var index = cursorAlbum.getString(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID))
                var str = cursorAlbum.getString(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
                if (str != null){
                    if(picture==index){
                        return str
                    }
                }
            } while (cursorAlbum.moveToNext())
            return ""
        }


    }


}