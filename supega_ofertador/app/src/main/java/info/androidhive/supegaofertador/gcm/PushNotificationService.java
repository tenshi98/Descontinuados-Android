package info.androidhive.supegaofertador.gcm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import info.androidhive.supegaofertador.R;
import info.androidhive.supegaofertador.activity.MainActivity;

public class PushNotificationService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String mensaje              = data.getString("message");

        //Se envia la notificacion a una clase personalizada
        notifyUser(this, mensaje);

    }

    public void notifyUser(Context context,String mensaje){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent( context, MainActivity.class);
        notificationIntent.putExtra("Notificaciones", "viewNotificaciones");
        Bitmap large = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_large);
        int small    = R.drawable.ic_launcher;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(mensaje)
                .setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setLargeIcon(large)
                .setSmallIcon(small)
                .setTicker(mensaje)
                .setWhen(System.currentTimeMillis())
                .build();

        notificationManager.notify(0, notification);





    }
}
