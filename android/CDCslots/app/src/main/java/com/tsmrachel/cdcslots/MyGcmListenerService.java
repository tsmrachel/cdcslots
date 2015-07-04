package com.tsmrachel.cdcslots;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {
    public MyGcmListenerService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d("cdcSlots", "From: " + from);
        Log.d("cdcSlots", "Message: " + message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
    }

    private void sendNotification(String message) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("CDC Slots")
                        .setContentText("Day/Session Availble");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
//        mNotificationManager.notify(mId, mBuilder.build());
        mNotificationManager.notify(0, mBuilder.build());

    }

}
