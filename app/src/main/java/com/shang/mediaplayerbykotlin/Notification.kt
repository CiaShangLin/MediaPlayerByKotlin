package com.shang.mediaplayerbykotlin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import com.shang.mediaplayerbykotlin.Activity.PlayMusicActivity
import com.shang.mediaplayerbykotlin.MP.MPC
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService

/**
 * Created by Shang on 2018/8/23.
 */
class Notification {

    companion object {

        val ID = 1
        lateinit var notificationChannel: NotificationChannel
        lateinit var notificationManager: NotificationManager
        lateinit var notificationBuilder: Notification.Builder
        lateinit var remoteViews: RemoteViews
        val channel_ID = "com.shang.mediaplayerbykotlin"


        fun showNotication(context: Context,name:String,picture: String) {

            remoteViews= getRemoteViews(context,name,picture)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationChannel = NotificationChannel(channel_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                notificationBuilder(context)
            } else {
                notificationBuilder(context)
            }

            notificationManager.notify(ID, notificationBuilder.build())

        }

        fun update(name:String,picture:String){
            remoteViews.setTextViewText(R.id.remoteNameTv,name)

            if(MPC.mediaPlayer!=null && MPC.mediaPlayer!!.isPlaying){
                remoteViews.setImageViewResource(R.id.remotePlayBt,R.drawable.ic_remote_pause)
            }else{
                remoteViews.setImageViewResource(R.id.remotePlayBt,R.drawable.ic_remote_play)
            }

            var bitmap=BitmapFactory.decodeFile(picture)
            if(bitmap==null){
                remoteViews.setImageViewResource(R.id.remoteIg,R.drawable.ic_music)
            }else{
                remoteViews.setImageViewBitmap(R.id.remoteIg,bitmap)
            }



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.notify(ID,notificationBuilder.build())
            } else {
                notificationManager.notify(ID,notificationBuilder.build())
            }
        }


        private fun notificationBuilder(context: Context){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                notificationBuilder = Notification.Builder(context, channel_ID).apply {

                    this.setSmallIcon(R.drawable.ic_music)
                    this.setLargeIcon(android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.ic_music))
                    this.setCustomContentView(remoteViews)

                }
            } else {
                notificationBuilder = Notification.Builder(context).apply {
                    this.setContent(remoteViews)
                    this.setSmallIcon(R.drawable.ic_music)
         
                }
            }
        }

        private fun getRemoteViews(context: Context,name:String,picture: String): RemoteViews {
            var remote= RemoteViews(context.packageName,R.layout.remote_view_layout)
            var bitmap=BitmapFactory.decodeFile(picture)
            if(bitmap==null){
                remote.setImageViewResource(R.id.remoteIg,R.drawable.ic_music)
            }else{
                remote.setImageViewBitmap(R.id.remoteIg,bitmap)
            }
            remote.setImageViewResource(R.id.remotePreBt,R.drawable.ic_previous)
            if(MPC.mediaPlayer!=null && MPC.mediaPlayer!!.isPlaying){
                remote.setImageViewResource(R.id.remotePlayBt,R.drawable.ic_remote_pause)
            }else{
                remote.setImageViewResource(R.id.remotePlayBt,R.drawable.ic_remote_play)
            }
            remote.setImageViewResource(R.id.remoteNextBt,R.drawable.ic_next)
            remote.setTextViewText(R.id.remoteNameTv,name)

            remote.setOnClickPendingIntent(R.id.remotePlayBt, getPendingIntent(context,PlayMusicActivity.PLAY))
            remote.setOnClickPendingIntent(R.id.remoteNextBt, getPendingIntent(context,PlayMusicActivity.NEXT))
            remote.setOnClickPendingIntent(R.id.remotePreBt, getPendingIntent(context,PlayMusicActivity.PREVIOUS))

            return remote
        }

        private fun getPendingIntent(context: Context,action:String): PendingIntent {
            var intent = Intent(context, MediaPlayerService::class.java).apply {
                this.action = action
            }

            var pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            return pendingIntent
        }


    }

}