package com.smarthome.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;
import com.smarthome.Services.ServiceNotify;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class CActivityMain extends AppCompatActivity {

    public Switch switch_temp;
    final String LOG_TAG  = "status";
    public static final String APP_PREFERENCES_SWITCH_TEMPERATURE = "settings";
    public static final String APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE = "switch_temp";
    private SharedPreferences mSettings;

    private static final int LAYOUT = R.layout.activity_main;
    private static final int THEME = R.style.AppDefault;
    private static final int MENU = R.menu.activity_main;
    private static final int TOOLBAR = R.id.toolbar;
    private static final int SEARCH =R.id.search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(THEME);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Log.d(LOG_TAG, "MainActivity: onCreate()");

        initToolbar();

        //запускаем сервис по получению сообщений от сервера NATS при наличии интернета
        startService(new Intent(this, ServiceNotify.class));

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
        Toolbar toolbar                     = findViewById(TOOLBAR);
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

    /****************************************************************************************************
     * Действия при создании меню.                                                                      *
     * @param menu - заготовка для меню.                                                                *
     * @return                                                                                          *
     ***************************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater               = getMenuInflater();
        inflater.inflate(MENU, menu);
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
            case SEARCH:

                return true;
//            case R.id.help:
//                showHelp();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}