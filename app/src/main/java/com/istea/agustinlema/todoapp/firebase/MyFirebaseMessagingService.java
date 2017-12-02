package com.istea.agustinlema.todoapp.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.activities.ListActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String title = notification.getTitle();
        String body = notification.getBody();
        showNotification(title, body);
        super.onMessageReceived(remoteMessage);
    }

    private void showNotification(String title, String body){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);



        Intent intent = new Intent(this,ListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);//pending intent
        builder.setContentIntent(pendingIntent);

        NotificationManager managerCompat = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//obtengo el servicio de notificaciones y se lo pongo al manager
        managerCompat.notify(2, builder.build());    //le paso un id y el builder

    }
}
