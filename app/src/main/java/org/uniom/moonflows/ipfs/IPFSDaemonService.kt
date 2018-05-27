package org.uniom.moonflows.ipfs

import android.app.IntentService
import android.app.Notification
import android.content.Intent
import android.support.v4.app.NotificationCompat
import org.uniom.moonflows.R
import android.os.Build



class IPFSDaemonService : IntentService("IPFSDaemonService") {

    private var daemon: Process? = null

    override fun onHandleIntent(intent: Intent) {
        try {
            State.isDaemonRunning = true
            daemon = IPFSDaemon(baseContext).run("daemon")
            daemon!!.waitFor()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
/*
        val Action = intent.getIntExtra("Action", 0);

        when (Action) {
            ServiceAction.RUN_DAEMON -> {
                try {
                    daemon = IPFSDaemon(baseContext).run("daemon")
                    State.isDaemonRunning = true
                    daemon!!.waitFor()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            ServiceAction.STOP_FOREGROUND -> {
                stopForeground (true)
            }
        }*/
    }

    override fun onDestroy() {
        stopForeground (true)
        if (State.isDaemonRunning){
            daemon!!.destroy()
        }

        super.onDestroy()
        State.isDaemonRunning = false
    }

    private val ANDROID_CHANNEL_ID = "IPFSDaemon"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val builder = Notification.Builder(this, ANDROID_CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("IPFS Daemon started")
                    .setAutoCancel(false)

            val notification = builder.build()
            startForeground(1, notification)

        } else {

            val builder = NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("IPFS Daemon started")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

            val notification = builder.build()

            startForeground(1, notification)
        }
        return super.onStartCommand(intent, flags, startId)

    }

}
