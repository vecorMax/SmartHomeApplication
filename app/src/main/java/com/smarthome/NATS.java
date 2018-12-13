package com.smarthome;

import java.io.IOException;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Statistics;
import io.nats.client.SyncSubscription;

public class NATS extends Thread {

    public static Connection natsConnection;
    public static SyncSubscription natsSubscription1;
    public static SyncSubscription natsSubscription2;

    @Override
    public void run() {
        try {
            if (natsConnection == null || natsConnection.getState().toString().equals("CLOSED"))
            {
                natsConnection = Nats.connect("nats://192.168.1.103:4222");
                System.out.print("OK");
                natsSubscription1 = natsConnection.subscribeSync("TR1", "queue");
                natsSubscription2 = natsConnection.subscribeSync("TR1C", "queue");
            }

            subscribe();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subscribe() throws IOException, InterruptedException {
        while (true) {
            Message message1 = natsSubscription1.nextMessage(2500);
            Message message2 = natsSubscription2.nextMessage(2500);
            if (message1 != null) {
                byte[] data = message1.getData();
                String str = new String(data, "UTF-8");
                System.out.print(str);
            }
            else if (message2 != null) {
                byte[] data = message2.getData();
                String str = new String(data, "UTF-8");
                System.out.print(str);
            }
        }
    }

    // завершение сеанса с сервером от сервера
    public void finish()
    {
        if (natsConnection != null)
        {
           natsConnection.close();
        }
    }

}
