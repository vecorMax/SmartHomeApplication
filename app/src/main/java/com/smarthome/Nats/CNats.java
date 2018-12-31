package com.smarthome.Nats;

import com.smarthome.Notifications.CNotifications;

import java.io.IOException;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.SyncSubscription;

public class CNats extends Thread {

    public static CNats mCNats; //объект класса CNats для подключение к серверу на плате RaspberryPi 3
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
                CNotifications.notifyBuilder(str,"Температура в помещении 1");
            }
            else if (message2 != null) {
                byte[] data = message2.getData();
                String str = new String(data, "UTF-8");
                System.out.print(str);
                CNotifications.notifyBuilder(str,"Temperature in Room 1 is changed");
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
   Устанавливает соединение с сервером CNats при наличии любого интернета на устройстве
    */
    public static void createConnectionNATSServer(int status)
    {
        if (status == 1 || status == 2)
        {
            if (mCNats == null || mCNats.getState() == Thread.State.TERMINATED)
            {
                mCNats = new CNats();
                mCNats.start();
            }
        }
        else
        {
            if (mCNats != null)
            {
                finish();
                try {
                    mCNats.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    Закрываем соединение с сервером CNats при завершение работы сервиса
     */
    public static void closeConnectionNATSServer()
    {
        if (mCNats != null)
        {
            finish();
            try {
                mCNats.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
