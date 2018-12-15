package com.smarthome.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.smarthome.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notify
{
    public static Context mContext;
    public static boolean getDataTemperatureInside; //создавать/не создавать уведомления по температуре состоянию switch
    // Идентификатор уведомления
    private static final int NOTIFY_ID_TEMPERATURE_INSIDE = 1;

    /*

     */
    public static void createNotificationTemperature()
    {
        if (getDataTemperatureInside)
        {

        }
    }

    /*
    Построитель уведомлений
     */
    public static void notifyBuilder(String title, String notifyText)
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_temperature_inside)
                        .setContentTitle(title)
                        .setContentText(notifyText);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
