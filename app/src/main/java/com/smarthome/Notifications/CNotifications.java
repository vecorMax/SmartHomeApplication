package com.smarthome.Notifications;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import com.smarthome.R;
import androidx.core.app.NotificationCompat;
import static android.content.Context.NOTIFICATION_SERVICE;

public class CNotifications
{


    public static void createNotification(String title, String notifyText, Context mContext){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_notify)
                        .setContentTitle(title)
                        .setContentText(notifyText);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}
