package com.smarthome.Nats;

import android.util.Log;
import java.io.IOException;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;

public class CServiceMessaging extends Thread
{
    private static final String LOG_TAG                 = "status";
    private static final String address = "nats://192.168.1.103:4222";
    private static Connection nats;

    @Override
    public void run() {
        connect();
    }

    public static void connect()  {
        if (nats == null || !nats.isConnected())
        {
            Log.d(LOG_TAG,"Establishing connection to NATS server.");
            try {
                nats = Nats.connect(address);
                Log.d(LOG_TAG, "Connection to NATS server established.");
                subscribe();
                Log.d(LOG_TAG, "Organized subscription to messages from NATS server");
            }
            catch (IOException e) {
                Log.d(LOG_TAG, "Cannot connect to NATS server.");
                e.printStackTrace();
            }
        }
    }


    public static void send(Message msg) {
        connect();
        if (!nats.isConnected())
            return;

        try {
            nats.publish("TEMP", msg.getData());
            Log.d(LOG_TAG, "Message was published!");
        } catch (IOException e) {
            Log.d(LOG_TAG, "Cannot publish message to NATS server.");
            e.printStackTrace();
        }
    }

    public static void close(){
        if(!nats.isConnected())
            return;

        Log.d(LOG_TAG, "Closing connection to NATS server.");
        nats.close();
        Log.d(LOG_TAG, "Connection to NATS server closed.");
    }

    public static void subscribe(){
        if(!nats.isConnected())
            return;

        nats.subscribe("TEMP"); //получение данных о текущей температуре с сервера NATS "главной" платы
    }
}
