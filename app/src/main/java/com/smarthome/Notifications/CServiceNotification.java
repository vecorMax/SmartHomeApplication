package com.smarthome.Notifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.smarthome.Nats.CNats;
import com.smarthome.Utils.CCheckNetworkState;

public class CServiceNotification extends Service {

    final String LOG_TAG = "status"; //логирование
    public static int connState; //статус сети Интернет на устройстве


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "CServiceNotification: onCreate()");
    }

    public void onDestroy() {
        super.onDestroy();
        CNats.closeConnectionNATSServer();
        Log.d(LOG_TAG, "CServiceNotification: onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "CServiceNotification: onBind()");
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "CServiceNotification: onStartCommand()");
        connState = stateNetwork();
        CNats.createConnectionNATSServer(connState);
        return super.onStartCommand(intent, flags, startId);
    }

    public int stateNetwork() {
        return CCheckNetworkState.getConnectivityStatus(getApplicationContext());
    }

}
