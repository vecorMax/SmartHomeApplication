package com.smarthome.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.smarthome.Activities.CActivityMain;
import com.smarthome.Activities.CActivityStart;
import com.smarthome.Nats.CNats;
import com.smarthome.Utils.CCheckNetworkState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class CServiceTools extends Service {

    private final String LOG_TAG = "status";

    public void onCreate() {
        Log.d(LOG_TAG, "CServiceTools: onCreate()");
        super.onCreate();
    }

    public void onDestroy() {
        Log.d(LOG_TAG, "CServiceTools: onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "CServiceTools: onBind()");
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService onStartCommand, name = " + intent.getStringExtra("name"));
        return START_STICKY;
    }

}
