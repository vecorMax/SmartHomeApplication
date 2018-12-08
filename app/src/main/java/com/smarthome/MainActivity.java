package com.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import android.widget.EditText;
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

    public static NATS mNats;
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
                    //инициализация получения данных от сервера NATS
                    if (mNats == null || mNats.getState() == Thread.State.TERMINATED)
                    {
                        mNats = new NATS();
                        mNats.start();
                    }
                    Toast.makeText(getApplicationContext(), "Уведомления установлено!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mNats != null)
                    {
                        mNats.finish();
                        try {
                            mNats.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Уведомление отключено!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}