package com.appmatic.baseapp.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Appmatic
 * Copyright (C) 2016 - Nulltilus
 *
 * This file is part of Appmatic.
 *
 * Appmatic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Appmatic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Appmatic.  If not, see <http://www.gnu.org/licenses/>.
 */

public class AppmaticMessagingService extends FirebaseMessagingService {
    private static final String DATA_TYPE = "data_type";
    private static final String DATA_TYPE_NOTIFICATION = "notification";

    private static final String NOTIFICATION_TITLE = "title";
    private static final String NOTIFICATION_BODY = "body";

    private static int notificationCount = 0;

    @Override
    public void handleIntent(Intent intent) {
        try {
            super.handleIntent(intent);
        } catch (AbstractMethodError ignored) {
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().get(DATA_TYPE) != null && remoteMessage.getData().get(DATA_TYPE).contains(DATA_TYPE_NOTIFICATION))
            sendNotification(remoteMessage.getData().get(NOTIFICATION_TITLE), remoteMessage.getData().get(NOTIFICATION_BODY));
    }

    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if (messageTitle == null) messageTitle = "";
        if (messageBody == null) messageBody = "";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLights(Color.WHITE, 1000, 1000)
                .setContentTitle(messageTitle.isEmpty() ? getString(R.string.app_name) : messageTitle)
                .setContentText(messageBody)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_fcm_notification)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_DEFAULT);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(messageTitle.isEmpty() ? getString(R.string.app_name) : messageTitle);
            bigTextStyle.bigText(messageBody);

            notificationBuilder.setStyle(bigTextStyle);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notificationToShow = notificationBuilder.build();
        notificationToShow.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(notificationCount++, notificationToShow);
    }

}
