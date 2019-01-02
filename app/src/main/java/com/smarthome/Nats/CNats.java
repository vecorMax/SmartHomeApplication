package com.smarthome.Nats;

import com.smarthome.Notifications.CNotifications;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;
import io.nats.client.SyncSubscription;

public class CNats extends Thread {

    private static final String address = "nats://192.168.1.103:4222";
    public static CNats mNats;
    public static Connection natsConnection;
    private static SyncSubscription natsSubs;
    private static SyncSubscription natsSubs2;

    @Override
    public void run() {
        try {
            if (natsConnection == null || natsConnection.getState().toString().equals("CLOSED")) {

                //установление соединения с сервером NATS на плате RPi3
                natsConnection = Nats.connect(address);

                //подписываемся на получение сообщений
                natsSubs = natsConnection.subscribeSync("TR1", "queue");
                natsSubs2 = natsConnection.subscribeSync("TR1C", "queue");
            }

            // получение сообщений от сервера NATS на плате RPi3 в бесконечном цикле
            subscribe();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void subscribe() throws IOException, InterruptedException {
        while (true) {

            Message message1 = natsSubs.nextMessage(0);
            Message message2 = natsSubs2.nextMessage(0);

            if (message1 != null)
            {
                String str = new String(message1.getData(), StandardCharsets.UTF_8);
                System.out.print(str);
            }
            else if (message2 != null)
            {
                String str = new String(message2.getData(), StandardCharsets.UTF_8);
                System.out.print(str);
            }
        }
    }

}




