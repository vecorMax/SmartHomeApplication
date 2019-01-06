package com.smarthome.Notifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.smarthome.Nats.CNats;
import com.smarthome.Utils.CCheckNetworkState;

/**
 * Сервис по предоставлению уведомлений пользователю из CActivityNotifications
 */
public class CServiceNotification extends Service {

    private final String LOG_TAG = "status"; //логирование

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "CServiceNotification: onCreate()");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "CServiceNotification: onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "CServiceNotification: onBind()");
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "CServiceNotification: onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

}
