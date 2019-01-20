package com.smarthome.Activities;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.smarthome.Nats.CServiceMessaging;
import com.smarthome.R;
import com.smarthome.Utils.CBroadcastReceiverConnectivity;
import com.smarthome.Utils.CBroadcastReceiverUpdating;
import com.smarthome.Utils.CCustomSharedPreference;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.smarthome.Utils.CBroadcastReceiverUpdating.PARAM_DATA;


public class CActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG                 = "status";
    public final static String BROADCAST_ACTION         = "com.smarthome.Activities";
    private static final int LAYOUT                     = R.layout.activity_main;
    private static final int THEME                      = R.style.AppDefault;
    private static final int TOOLBAR                    = R.id.toolbar;
    private static final int DRAW_LAYOUT                = R.id.drawerLayout;
    private static final int LOGOUT                     = R.id.Logout;
    private static final int HOMEPAGE                   = R.id.HomePage;
    private static final int NOTIFY                     = R.id.Notify;

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public static CBroadcastReceiverConnectivity cBroadcastReceiverConnectivity;
    public static CBroadcastReceiverUpdating cBroadcastReceiverUpdating;
    public static CServiceMessaging cServiceMessaging;

    TextView textTemperature;
    TextView dataTemperature;

    public static Handler h;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(THEME);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Log.d(LOG_TAG, "CActivityMain: onCreate()");

        init();
        initToolbar();

        h = new Handler() {
            public void handleMessage(Message msg) {
                // обновляем TextView
                dataTemperature.setText(msg.getData().getString(PARAM_DATA));
            }
        };

    }

    private void init(){
        drawerLayout                                        = findViewById(DRAW_LAYOUT);
        navigationView                                      = findViewById(R.id.navigation);
        textTemperature                                     = findViewById(R.id.txt_notify_temperature);
        dataTemperature                                     = findViewById(R.id.dataTemperature);

        navigationView.setNavigationItemSelectedListener(this);
        if (cServiceMessaging == null) {
            cServiceMessaging = new CServiceMessaging();
        }

        //BroadcastReceiverConnectivity для отслеживания состояния сети Интернет на телефоне
        if (cBroadcastReceiverConnectivity == null){
            cBroadcastReceiverConnectivity = new CBroadcastReceiverConnectivity();
            IntentFilter intentFilterConnectivity = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(cBroadcastReceiverConnectivity, intentFilterConnectivity);
        }

        //BroadcastReceiverUpdating для обновления данных на странице приложения
        if (cBroadcastReceiverUpdating == null) {
            cBroadcastReceiverUpdating = new CBroadcastReceiverUpdating();
            IntentFilter intentFilterUpdating = new IntentFilter(BROADCAST_ACTION);
            registerReceiver(cBroadcastReceiverUpdating, intentFilterUpdating);
        }

    }

    private void initToolbar() {
        Toolbar toolbar                                     = findViewById(TOOLBAR);
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

        switch (id)
        {
            case HOMEPAGE: // Возврат на главную страницу приложения
                userHomePage();
                break;
            case NOTIFY:  // Переход на страницу с уведомлениями
                userNotify();
                break;
            case LOGOUT: // Управление выходом из учетной записи пользователя
                userLogOut();
                break;
            default:
                return super.onOptionsItemSelected(item);
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
        CCustomSharedPreference.setLoginData(false);
        unregisterReceiver(cBroadcastReceiverConnectivity);
        unregisterReceiver(cBroadcastReceiverUpdating);
        startActivity(new Intent(this, CActivityStart.class));
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
     * Вернутся на страницу управления уведомлениями                                                    *
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
        dataTemperature.setText(savedInstanceState.getString("dataTemperature"));
        Log.d(LOG_TAG, "CActivityMain: onRestoreInstanceState()");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("dataTemperature",dataTemperature.getText().toString());
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
        //finish();
    }

}