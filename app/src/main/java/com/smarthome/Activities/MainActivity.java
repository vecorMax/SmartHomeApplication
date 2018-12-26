package com.smarthome.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.smarthome.NATS;
import com.smarthome.Notifications.Notify;
import com.smarthome.R;
import com.smarthome.Services.ServiceNotify;

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

    public Switch switch_temp;
    final String LOG_TAG  = "status";
    public static final String APP_PREFERENCES_SWITCH_TEMPERATURE = "settings";
    public static final String APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE = "switch_temp";
    private SharedPreferences mSettings;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         Log.d(LOG_TAG,"MainActivity: onCreate()");

        initToolbar();

        //запускаем сервис по получению сообщений от сервера NATS при наличии интернета
        startService(new Intent(this,ServiceNotify.class));

        // экземпляр класса SharedPreferences, который отвечает за работу с настройками
        mSettings = getSharedPreferences(APP_PREFERENCES_SWITCH_TEMPERATURE, Context.MODE_PRIVATE);

        //Инициализация элементов activity_main
//        switch_temp = findViewById(R.id.switch_Temperature);

        // добавляем слушателя переключателя
//        switch_temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    //Рассылка уведомлений о получений данных о погоде установлена
//                    Notify.getDataTemperatureInside = true;
//                    Notify.mContext = getApplicationContext();
//                } else {
//                    //Рассылка уведомлений о получении данных о погоде установлена
//                    Notify.getDataTemperatureInside = false;
//                }
//            }
//        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"MainActivity: onResume()");
        if (mSettings.contains(APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE)) {
            // Получаем данные из настроек
            switch_temp.setChecked(mSettings.getBoolean(APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE, false));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"MainActivity: onPause()");
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE, switch_temp.isChecked());
        editor.apply();
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        switch_temp.setChecked(savedInstanceState.getBoolean("switch"));
        Log.d(LOG_TAG, "MainActivity: onRestoreInstanceState()");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("switch",switch_temp.isChecked());
        Log.d(LOG_TAG, "MainActivity: onSaveInstanceState()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"MainActivity: onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "MainActivity: onRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"MainActivity: onDestroy()");
    }

}