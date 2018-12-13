package com.smarthome.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceNotify extends Service {
    public ServiceNotify() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
