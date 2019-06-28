package br.com.matheus.coroutinetest.background

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import br.com.matheus.coroutinetest.ParadaActivity
import br.com.matheus.coroutinetest.R
import br.com.matheus.coroutinetest.model.Parada

class LocationService : IntentService("aa") {

    private val NOTIFICATION_ID = "1258"
    private val FOREGROUND_ID = 1200
    private var run = true



    override fun onDestroy() {
        Log.i("LocationService", "LocationService destruido!")
        super.onDestroy()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {
        Log.i("LocationService", "LocationService onHandleItent()")

        criarNotificationChannel()
        criarNotificacao()

        val parada = intent?.extras?.getSerializable("parada") as Parada

        val locationLogic = LocationLogic(applicationContext, parada){
            run = false
        }

        locationLogic.distanciaDoisPontos(this.mainLooper)

        while (run){
            Log.i("LocationService", "LocationService running...")
            Thread.sleep(1000)
        }

        stopForeground(true)
    }


    private fun criarNotificacao(){
        /*val notif = Notification(R.drawable.ic_launcher_foreground, "Parada Service", System.currentTimeMillis())
        val notifIntent = Intent(this,ParadaActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,notifIntent,0)
        startForeground(FOREGROUND_ID,notif)*/

        val notif = NotificationCompat.Builder(this,NOTIFICATION_ID)

        notif.setContentTitle("Parada Service")
        notif.setContentText("Você será avisado quando chegar perto da parada.")
        notif.setSmallIcon(R.drawable.ic_launcher_foreground)

        val notifIntent = Intent(this,ParadaActivity::class.java)
        //val pendingIntent = PendingIntent.getActivity(this,0,notifIntent,0)
        startForeground(FOREGROUND_ID,notif.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun criarNotificationChannel(){
        val channel = NotificationChannel(NOTIFICATION_ID, "Parada Service", NotificationManager.IMPORTANCE_HIGH)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }

}