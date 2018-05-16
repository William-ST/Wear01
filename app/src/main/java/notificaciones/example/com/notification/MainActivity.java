package notificaciones.example.com.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;
    final static String MI_GRUPO_DE_NOTIFIC = "mi_grupo_de_notific";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intencionMapa = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=universidad+politecnica+valencia"));
        final PendingIntent intencionPendienteMapa = PendingIntent.getActivity(MainActivity.this, 0, intencionMapa, 0);

        Intent intencionLlamar = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:555123456"));
        final PendingIntent intencionPendienteLlamar = PendingIntent.getActivity(MainActivity.this, 0, intencionLlamar, 0);

        final NotificationCompat.Action acc = new NotificationCompat.Action.Builder(R.mipmap.ic_action_call,
                "llamar Wear", intencionPendienteLlamar).build();

        //Creamos una lista de acciones
        final List<NotificationCompat.Action> acciones = new ArrayList<NotificationCompat.Action>();
        acciones.add(acc);
        acciones.add(new NotificationCompat.Action(R.mipmap.ic_action_locate, "Ver mapa", intencionPendienteMapa));


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CANAL_ID, "Mis Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 100, 300, 100});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Button wearButton = (Button) findViewById(R.id.boton1);
        wearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "Texto largo con descripción detallada de la notificación. ";

                // Creamos un BigTextStyle para la segunda página
                NotificationCompat.BigTextStyle segundaPg = new NotificationCompat.BigTextStyle();
                segundaPg.setBigContentTitle("Página 2")
                        .bigText("Más texto.");

                List<Notification> notificationPages = new ArrayList<>();

                Notification notificacionPg2 = new NotificationCompat.Builder( MainActivity.this)
                        .setStyle(segundaPg)
                        .build();
                Notification notificacionPg3 = new NotificationCompat.Builder( MainActivity.this)
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("Página 3").bigText("Más texto númeor 3 ..."))
                        .build();

                notificationPages.add(notificacionPg2);
                notificationPages.add(notificacionPg3);

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.escudo_upv))
                        .addActions(acciones)
                        .addPages(notificationPages);

                NotificationCompat.Builder notificacion = new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Título")
                        .setContentText(Html.fromHtml("Notificación Android <i>Wear</i>")) //<b>Notificación</b> <u>Android <i>Wear</i></u>
                        //.setContentIntent(intencionPendienteMapa)
                        .addAction(R.mipmap.ic_action_call, "llamar", intencionPendienteLlamar)

                        //.extend(new NotificationCompat.WearableExtender()
                        //        .addActions(acciones))

                        //.setLargeIcon(BitmapFactory.decodeResource( getResources(), R.drawable.escudo_upv))
                        .setStyle(segundaPg)//(new NotificationCompat.BigTextStyle().bigText(s + s + s + s))
                        .extend(wearableExtender)
                        .setGroup(MI_GRUPO_DE_NOTIFIC);

                notificationManager.notify(NOTIFICACION_ID, notificacion.build());




                int idNotificacion2 = 002;
                NotificationCompat.Builder notificacion2 = new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                        .setContentTitle("Nueva Conferencia")
                        .setContentText("Los neutrinos")
                        .setSmallIcon(R.mipmap.ic_action_mail_add)
                        .setGroup(MI_GRUPO_DE_NOTIFIC);
                notificationManager.notify(idNotificacion2, notificacion2.build());
            }
        });

    }
}
