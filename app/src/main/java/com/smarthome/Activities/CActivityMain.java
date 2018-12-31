package com.smarthome.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;

import com.google.android.material.tabs.TabLayout;
import com.smarthome.R;
import com.smarthome.Utils.CSharedPreferences;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class CActivityMain extends AppCompatActivity {

    private static final String LOG_TAG                 = "status";
    private static final int LAYOUT                     = R.layout.activity_main;
    private static final int THEME                      = R.style.AppDefault;
    private static final int TOOLBAR                    = R.id.toolbar;
    private static final int MENU_MAIN                  = R.menu.activity_main;
    private static final int SEARCH                     = R.id.search;
    private static final int DRAW_LAYOUT                = R.id.drawerLayout;
    private static final int MENU_NAVI                  = R.menu.menu_navigation;


    public Switch switch_temp;
    public static final String APP_PREFERENCES_SWITCH_TEMPERATURE = "settings";
    public static final String APP_PREFERENCES_COUNTER_SWITCH_TEMPERATURE = "switch_temp";

    private SharedPreferences mSettings;
    private DrawerLayout drawerLayout;

    private CSharedPreferences cSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(THEME);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Log.d(LOG_TAG, "MainActivity: onCreate()");


        cSharedPreferences = new CSharedPreferences(getApplicationContext());
        initToolbar();
        initNavigationView();

        //запускаем сервис по получению сообщений от сервера CNats при наличии интернета
        //startService(new Intent(this, CServiceNotification.class));

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
//                    CNotifications.getDataTemperatureInside = true;
//                    CNotifications.mContext = getApplicationContext();
//                } else {
//                    //Рассылка уведомлений о получении данных о погоде установлена
//                    CNotifications.getDataTemperatureInside = false;
//                }
//            }
//        });
    }

    private void initNavigationView() {
        drawerLayout = findViewById(DRAW_LAYOUT);
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
        inflater.inflate(MENU_MAIN, menu);
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
            case R.id.Logout:
                userLogOut();
                return true;
            case R.id.Settings:

                return true;
            case R.id.Notify:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /****************************************************************************************************
     * Выход из учетной записи пользователя                                                             *
     * @param                                                                                           *
     * @return                                                                                          *
     ***************************************************************************************************/
    public void userLogOut() {
        cSharedPreferences.writeLoginStatus(false);
        startActivity(new Intent(this,CActivityLogin.class));
        finish();
    }

}