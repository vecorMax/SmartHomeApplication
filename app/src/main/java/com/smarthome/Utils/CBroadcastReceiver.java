package com.smarthome.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.smarthome.Activities.CActivityMain;
import com.smarthome.Nats.CNats;
import com.smarthome.Notifications.CNotifications;

import java.util.concurrent.TimeUnit;

import static com.smarthome.Utils.CCheckNetworkState.TYPE_MOBILE;
import static com.smarthome.Utils.CCheckNetworkState.TYPE_NOT_CONNECTED;
import static com.smarthome.Utils.CCheckNetworkState.TYPE_WIFI;

public class CBroadcastReceiver extends BroadcastReceiver
{
    private static final String LOG_TAG                     = "status";

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = CCheckNetworkState.getConnectivityStatus(context);
        switch (status) {
            case TYPE_WIFI: //наличие сети Интернет Wi-Fi
                Toast.makeText(context, "WiFi is ON", Toast.LENGTH_SHORT).show();
                try {
                    //задержка на 15 секунд, для подлключения к RaspberryPi3
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CActivityMain.startNATS(context); //запуск подклчючения к серверу NATS
                break;
            case TYPE_MOBILE: //наличие мобильной сети Интернет
                Toast.makeText(context, "WiFi is ON (Mobile)", Toast.LENGTH_SHORT).show();
                CActivityMain.startNATS(context); //запуск подклчючения к серверу NATS
                break;
            case TYPE_NOT_CONNECTED: //отсутствие сети Интернет
                Toast.makeText(context, "WiFi is OFF", Toast.LENGTH_SHORT).show();
                CActivityMain.closeNATS(context); //отключение соединения с сервером NATS
                break;
        }
    }
}
