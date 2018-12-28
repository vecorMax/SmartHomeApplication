package com.smarthome.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.smarthome.R;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;



public class Notify
{
    private static final int TEMP_INSIDE                            = R.mipmap.ic_temperature_inside;
    public static Context mContext;

    /*
    Построитель уведомлений
     */
    public static void notifyBuilder(String title, String notifyText)
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(TEMP_INSIDE)
                        .setContentTitle(title)
                        .setContentText(notifyText);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
