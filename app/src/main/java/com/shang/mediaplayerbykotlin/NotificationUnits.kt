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
class NotificationUnits {

    companion object {

        val Notification_ID = 906
        lateinit var channel: NotificationChannel
        lateinit var manager: NotificationManager
        var notiUnits: NotificationUnits? = null
        val channel_ID = "com.shang.mediaplayerbykotlin"


        fun instance(context: Context): NotificationUnits {

            if (notiUnits == null) {
                notiUnits = NotificationUnits()
                manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    channel = NotificationChannel(channel_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW)
                    channel.enableLights(false)
                    channel.enableVibration(false)
                    manager.createNotificationChannel(channel)
                }
            }
            return notiUnits!!
        }
    }


    fun notificationBuilder(context: Context, name: String, picture: String): Notification.Builder {
        var builder: Notification.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = Notification.Builder(context).apply {
                this.setSmallIcon(R.drawable.ic_music)
                this.setLargeIcon(android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.ic_music))
                this.setCustomContentView(getRemoteViews(context, name, picture))
                this.setChannelId(channel_ID)
                this.setAutoCancel(true)
            }
        } else {
            builder = Notification.Builder(context).apply {
                this.setContent(getRemoteViews(context, name, picture))
                this.setSmallIcon(R.drawable.ic_music)
                this.setAutoCancel(true)
            }
        }

        return builder
    }

    fun getRemoteViews(context: Context, name: String, picture: String): RemoteViews {
        var remote = RemoteViews(context.packageName, R.layout.remote_view_layout)

        var bitmap = BitmapFactory.decodeFile(picture)
        if (bitmap == null) {
            remote.setImageViewResource(R.id.remoteIg, R.drawable.ic_music)
        } else {
            remote.setImageViewBitmap(R.id.remoteIg, bitmap)
        }
        remote.setImageViewResource(R.id.remotePreBt, R.drawable.ic_previous)
        if (MPC.mediaPlayer != null && MPC.mediaPlayer!!.isPlaying) {
            remote.setImageViewResource(R.id.remotePlayBt, R.drawable.ic_remote_pause)
        } else {
            remote.setImageViewResource(R.id.remotePlayBt, R.drawable.ic_remote_play)
        }
        remote.setImageViewResource(R.id.remoteNextBt, R.drawable.ic_next)
        remote.setTextViewText(R.id.remoteNameTv, name)

        remote.setOnClickPendingIntent(R.id.remotePlayBt, getPendingIntent(context, PlayMusicActivity.PLAY))
        remote.setOnClickPendingIntent(R.id.remoteNextBt, getPendingIntent(context, PlayMusicActivity.NEXT))
        remote.setOnClickPendingIntent(R.id.remotePreBt, getPendingIntent(context, PlayMusicActivity.PREVIOUS))

        return remote
    }

    fun getPendingIntent(context: Context, action: String): PendingIntent {
        var intent = Intent(context, MediaPlayerService::class.java).apply {
            this.action = action
        }

        var pendingIntent: PendingIntent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getForegroundService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return pendingIntent
    }

    fun update(context: Context, name: String, picture: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel)
        }
        manager.notify(Notification_ID, notificationBuilder(context, name, picture).build())
    }


}