package com.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.Statistics;
import io.nats.client.Subscription;
import io.nats.client.SyncSubscription;

public class MainActivity extends AppCompatActivity {

    public static Connection natsConnection;
    public static SyncSubscription natsSubscription1;
    public static SyncSubscription natsSubscription2;
    public static NatsGo natsGo;
    public static int statusInternet;
    public Switch switch_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Инициализация элементов activity_main
        switch_temp = findViewById(R.id.switch_Temperature);

        // добавляем слушателя переключателя
        switch_temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // в зависимости от значения isChecked выводим нужное сообщение
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Уведомления установлено!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Уведомление отключено!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initNats()
    {
        natsGo = new NatsGo();
        natsGo.start();
    }

    class NatsGo extends Thread {
        @Override
        public void run() {
            initConnection();
        }

        public void initConnection() {
            try {
                natsConnection = Nats.connect("nats://192.168.1.103:4222");
                System.out.print("OK");
                subscribe();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void subscribe() throws IOException, InterruptedException {
            natsSubscription1 = natsConnection.subscribeSync("TR1", "queue");
            natsSubscription2 = natsConnection.subscribeSync("TR1C", "queue");

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
    }
}