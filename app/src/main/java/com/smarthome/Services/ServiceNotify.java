package com.smarthome.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.smarthome.NATS;
import com.smarthome.Utils.checkNetworkState;

public class ServiceNotify extends Service {

    final String LOG_TAG = "status"; //логирование
    public static int connState; //статус сети Интернет на устройстве


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "ServiceNotify: onCreate()");
    }

    public void onDestroy() {
        super.onDestroy();
        NATS.closeConnectionNATSServer();
        Log.d(LOG_TAG, "ServiceNotify: onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "ServiceNotify: onBind()");
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "ServiceNotify: onStartCommand()");
        connState = stateNetwork();
        NATS.createConnectionNATSServer(connState);
        return super.onStartCommand(intent, flags, startId);
    }

    /*
    Возвращает значение статуса сети
    0 - Соединение не установлено
    1 - Соединение установлено по сети WiFi
    2 - Соединение устаноавлено по мобильной сети Интернет
     */
    public int stateNetwork() {
        return checkNetworkState.getConnectivityStatus(getApplicationContext());
    }

}
