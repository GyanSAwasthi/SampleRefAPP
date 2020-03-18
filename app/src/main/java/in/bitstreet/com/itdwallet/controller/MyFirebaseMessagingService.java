package in.bitstreet.com.itdwallet.controller;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.bitstreet.com.itdwallet.R;

import static com.weiwangcn.betterspinner.library.material.R.attr.title;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
 private static final String TAG = "FCM Service";
 @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.


    /* Notification notification = new NotificationCompat.Builder(this)
             .setContentTitle(remoteMessage.getNotification().getTitle())
             .setContentText(remoteMessage.getNotification().getBody())
             .setSmallIcon(R.mipmap.ic_launcher)
             .build();
     NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
     manager.notify(123, notification);*/

     Notification notification = new NotificationCompat.Builder(this)
             .setContentTitle(remoteMessage.getData().get("title"))
             .setContentText(remoteMessage.getData().get("body"))
             .setSmallIcon(R.mipmap.ic_launcher)
             .build();
     NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
     manager.notify(123, notification);


        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}