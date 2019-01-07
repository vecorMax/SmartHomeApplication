package com.smarthome.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.smarthome.Nats.CNats;
import com.smarthome.Notifications.CNotifications;
import com.smarthome.Notifications.CServiceNotification;
import com.smarthome.R;
import com.smarthome.Utils.CSharedPreferences;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import static com.smarthome.Nats.CNats.mNats;
import static com.smarthome.R.string.login_preference;
import static com.smarthome.R.string.login_status_preferences;


public class CActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG                 = "status";
    private static final int LAYOUT                     = R.layout.activity_main;
    private static final int THEME                      = R.style.AppDefault;
    private static final int TOOLBAR                    = R.id.toolbar;
    private static final int DRAW_LAYOUT                = R.id.drawerLayout;
    private static final int LOGOUT                     = R.id.Logout;
    private static final int HOMEPAGE                   = R.id.HomePage;
    private static final int NOTIFY                     = R.id.Notify;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CSharedPreferences cSharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(THEME);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Log.d(LOG_TAG, "CActivityMain: onCreate()");

        init();
        initToolbar();
        //initNATS();

        // экземпляр класса SharedPreferences, который отвечает за работу с настройками
        //mSettings = getSharedPreferences(APP_PREFERENCES_SWITCH_TEMPERATURE, Context.MODE_PRIVATE);

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

    /**
     * Подключение к серверу NATS при успешном входе в учетную запись пользователя
     */
    public static void initNATS() {
        if (mNats == null || mNats.getState() == Thread.State.TERMINATED) {
            mNats = new CNats();
            mNats.start();
        }
    }

    private void init(){
        cSharedPreferences                                  = new CSharedPreferences(getApplicationContext(), login_preference);
        drawerLayout                                        = findViewById(DRAW_LAYOUT);
        navigationView                                      = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initToolbar() {
        Toolbar toolbar                     = findViewById(TOOLBAR);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Home");
        }
    }

    /****************************************************************************************************
     * Обработчик нажатия на элемент меню                                                               *
     * @param                                                                                           *
     * @return                                                                                          *
     ***************************************************************************************************/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == HOMEPAGE){
            // Возврат на главную страницу приложения
            userHomePage();
        }
        else if (id == NOTIFY){
            // Переход на страницу с уведомлениями
            userNotify();
        }
        else if (id ==  LOGOUT) {
            // Управление выходом из учетной записи пользователя
            userLogOut();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /****************************************************************************************************
     * Обработчик нажатия кнопки Назад из меню                                                          *
     * @param                                                                                           *
     * @return                                                                                          *
     ***************************************************************************************************/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /****************************************************************************************************
     * Выход из учетной записи пользователя                                                             *
     * @param                                                                                           *
     * @return                                                                                          *
     ***************************************************************************************************/
    public void userLogOut()  {
        cSharedPreferences.writeData(false, login_status_preferences);
        stopService(new Intent(this,CNotifications.class));
        startActivity(new Intent(this, CActivityLogin.class));
        finish();
    }

    /****************************************************************************************************
     * Вернутся на начальную страницу                                                                   *
     * @param                                                                                           *
     * @return                                                                                          *
     ***************************************************************************************************/
    public void userHomePage() {
        onBackPressed();
    }

    /****************************************************************************************************
     * Вернутся на начальную страницу                                                                   *
     * @param                                                                                           *
     * @return                                                                                          *
     ***************************************************************************************************/
    public void userNotify() {
        startActivity(new Intent(this, CActivityNotifications.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"CActivityMain: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"CActivityMain: onPause()");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //switch_temp.setChecked(savedInstanceState.getBoolean("switch"));
        Log.d(LOG_TAG, "CActivityMain: onRestoreInstanceState()");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean("switch",switch_temp.isChecked());
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

//    /****************************************************************************************************
//     * Действия при создании меню.                                                                      *
//     * @param menu - заготовка для меню.                                                                *
//     * @return                                                                                          *
//     ***************************************************************************************************/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater inflater               = getMenuInflater();
//        inflater.inflate(MENU_MAIN, menu);
//        return true;
//    }

//    /****************************************************************************************************
//     * Обработка нажатий на элемент меню.                                                               *
//     * @param item - элемент меню, на который нажал пользователь.                                       *
//     * @return                                                                                          *
//     ***************************************************************************************************/
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle item selection
//        switch (item.getItemId())
//        {
//            case LOGOUT:
//                userLogOut();
//                return true;
//            case R.id.Settings:
//                return true;
//            case R.id.Notify:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}