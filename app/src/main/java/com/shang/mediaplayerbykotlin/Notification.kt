package com.shang.mediaplayerbykotlin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.NotificationCompat
import android.widget.RemoteViews
import com.shang.mediaplayerbykotlin.MP.MediaPlayerService

/**
 * Created by Shang on 2018/8/23.
 */
class Notification {

    companion object {

        val ID = 1
        fun showNotication(context: Context) {
            var notificationChannel: NotificationChannel
            var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var notificationBuilder: Notification.Builder
            val channel_ID = "com.shang.mediaplayerbykotlin"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder = Notification.Builder(context, channel_ID)
                notificationChannel = NotificationChannel(channel_ID, "Test", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                notificationBuilder = Notification.Builder(context).apply {
                    this.setContentTitle("Title")
                    this.setContentText("TEST")
                    this.setSmallIcon(R.drawable.ic_favorite)
                    this.setLargeIcon(android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.ic_music))
                }

            } else {
                notificationBuilder = Notification.Builder(context).apply {
                    this.setContentTitle("Title")
                    this.setContentText("TEST")
                    this.setSmallIcon(R.drawable.ic_favorite)
                    this.setLargeIcon(android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.ic_music))
                }
            }


            notificationManager.notify(123, notificationBuilder.build())
            /* var remote= RemoteViews(context.packageName,R.layout.remote_view_layout)
                     remote.setImageViewResource(R.id.remoteIg,R.drawable.ic_music)
                     remote.setImageViewResource(R.id.remotePreBt,R.drawable.ic_previous)
                     remote.setImageViewResource(R.id.remotePlayBt,R.drawable.ic_remote_play)
                     remote.setImageViewResource(R.id.remoteNextBt,R.drawable.ic_next)
                     remote.setImageViewResource(R.id.remoteCancelBt,R.drawable.ic_cancel)
                     remote.setTextViewText(R.id.remoteNameTv,"Song Name")
                     remote.setTextViewText(R.id.remoteTimeTv,"03:14")

                     remote.setOnClickPendingIntent(R.id.remoteCancelBt, getPendingIntent(context))

                     var notification= NotificationCompat.Builder(context).apply {
                         this.setSmallIcon(R.drawable.ic_music)
                         this.setContent(remote)
                     }.build()

                     val no=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                     no.notify(ID,notification)*/

        }

        fun getPendingIntent(context: Context): PendingIntent {
            var intent = Intent(context, MediaPlayerService::class.java).apply {
                this.action = PlayMusicActivity.PAUSE
            }
            var pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            return pendingIntent
        }


    }

}