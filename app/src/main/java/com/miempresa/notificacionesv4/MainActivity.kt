package com.miempresa.notificacionesv4

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.miempresa.notificacionesv4.R.color.black
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.btnVerNotificacion

class MainActivity : AppCompatActivity() {
    var ID = 1
    val ONESIGNAL_APP_ID = "a7ffd40a-5fb8-4733-8ac6-169d7259de8f"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        btnVerNotificacion.setOnClickListener(){
            notificacionOreo()
        }
    }
    fun crearCanalNotificacion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.channel_id),name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun notificacionOreo(){
        crearCanalNotificacion()

        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.bigText("Este es el contenido expandido de la notificación. Este es el segundo contenido expandido de la notificación. Este es el tercer contenido expandido de la notificación. Este es el cuarto contenido expandido de la notificación.")

        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val mBuilder =
            NotificationCompat.Builder(this,getString(R.string.channel_id))
                .setSmallIcon(R.drawable.notificacion)
                .setContentTitle("Notificacion Oreo")
                //.setContentText("Mi primera notificacion en Oreo")
                .setStyle(bigTextStyle)
                .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.prueba_sonido))
                .setColor(resources.getColor(R.color.noti))  // Color naranja

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(ID++,mBuilder.build())
    }
}