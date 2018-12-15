package com.smarthome;

import com.smarthome.Notifications.Notify;

import java.io.IOException;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Statistics;
import io.nats.client.SyncSubscription;

public class NATS extends Thread {

    public static NATS mNats; //объект класса NATS для подключение к серверу на плате RaspberryPi 3
    public static Connection natsConnection;
    public static SyncSubscription natsSubscription1;
    public static SyncSubscription natsSubscription2;

    @Override
    public void run() {
        try {
            if (natsConnection == null || natsConnection.getState().toString().equals("CLOSED"))
            {
                natsConnection = Nats.connect("nats://192.168.1.103:4222");
                //подписываемся на получение сообщений
                natsSubscription1 = natsConnection.subscribeSync("TR1", "queue");
                natsSubscription2 = natsConnection.subscribeSync("TR1C", "queue");
            }
            subscribe();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void subscribe() throws IOException, InterruptedException {
        while (true) {
            Message message1 = natsSubscription1.nextMessage(2500);
            Message message2 = natsSubscription2.nextMessage(2500);
            if (message1 != null) {
                byte[] data = message1.getData();
                String str = new String(data, "UTF-8");
                System.out.print(str);
                Notify.notifyBuilder(str,"Температура в помещении 1");
            }
            else if (message2 != null) {
                byte[] data = message2.getData();
                String str = new String(data, "UTF-8");
                System.out.print(str);
                Notify.notifyBuilder(str,"Температура в помещении 1");
            }
        }
    }

    // завершение сеанса с сервером
    public static void finish()
    {
        if (natsConnection != null)
        {
           natsConnection.close();
        }
    }

    /*
   Устанавливает соединение с сервером NATS при наличии любого интернета на устройстве
    */
    public static void createConnectionNATSServer(int status)
    {
        if (status == 1 || status == 2)
        {
            if (mNats == null || mNats.getState() == Thread.State.TERMINATED)
            {
                mNats = new NATS();
                mNats.start();
            }
        }
        else
        {
            if (mNats != null)
            {
                finish();
                try {
                    mNats.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    Закрываем соединение с сервером NATS при завершение работы сервиса
     */
    public static void closeConnectionNATSServer()
    {
        if (mNats != null)
        {
            finish();
            try {
                mNats.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
