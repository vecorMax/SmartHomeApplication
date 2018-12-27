package com.smarthome.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.smarthome.R;
import com.smarthome.Services.ServiceNotify;



public class CActivityMain extends AppCompatActivity {

    public Switch switch_temp;
    final String LOG_TAG  = "status";
    public static final String APP_PREFERENCES_SWITCH_TEMPERATURE = "settings";
    public static final String APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE = "switch_temp";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         Log.d(LOG_TAG,"CActivityMain: onCreate()");

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
        Toolbar toolbar                     = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Welcome");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"CActivityMain: onResume()");
        if (mSettings.contains(APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE)) {
            // Получаем данные из настроек
            switch_temp.setChecked(mSettings.getBoolean(APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE, false));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"CActivityMain: onPause()");
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
//        editor.putBoolean(APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE, switch_temp.isChecked());
        editor.apply();
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        switch_temp.setChecked(savedInstanceState.getBoolean("switch"));
        Log.d(LOG_TAG, "CActivityMain: onRestoreInstanceState()");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean("switch",switch_temp.isChecked());
        Log.d(LOG_TAG, "CActivityMain: onSaveInstanceState()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"CActivityMain: onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "CActivityMain: onRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"CActivityMain: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"CActivityMain: onDestroy()");
    }

    /****************************************************************************************************
     * Действия при создании меню.                                                                      *
     * @param menu - заготовка для меню.                                                                *
     * @return                                                                                          *
     ***************************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater               = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
    /****************************************************************************************************
     * Обработка нажатий на элемент меню.                                                               *
     * @param item - элемент меню, на который нажал пользователь.                                       *
     * @return                                                                                          *
     ***************************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.search:

                return true;
//            case R.id.help:
//                showHelp();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}