package com.smarthome.Activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.smarthome.Nats.CServiceMessagingNats;
import com.smarthome.R;
import com.smarthome.Utils.CBroadcastReceiverConnectivity;
import com.smarthome.Utils.CCustomApplication;
import com.smarthome.SharedPreferences.CCustomSharedPreference;
import com.smarthome.SharedPreferences.CHomeSharedPreferences;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.smarthome.Nats.CServiceMessagingNats.PARAM_DATA;
import static com.smarthome.Nats.CServiceMessagingNats.cServiceMessagingNats;

public class CActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG                     = "status";
    public final static String BROADCAST_ACTION             = "com.smarthome.Activities";
    private static final int LAYOUT                         = R.layout.activity_main;
    private static final int THEME                          = R.style.AppDefault;
    private static final int TOOLBAR                        = R.id.toolbar;
    private static final int DRAW_LAYOUT                    = R.id.drawerLayout;
    private static final int LOGOUT                         = R.id.Logout;
    private static final int HOMEPAGE                       = R.id.HomePage;
    private static final int NOTIFY                         = R.id.Notify;
    private static final int NAVI_VIEW                      = R.id.navigation;
    private static final int SETTINGS                       = R.id.Settings;
    private static final int MAIN_MENU                      = R.menu.activity_main;
    private static final int REFRESH                        = R.id.refresh_data;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public CBroadcastReceiverConnectivity cBroadcastReceiverConnectivity;
    private BroadcastReceiver brUpdatingItems;

    protected static CHomeSharedPreferences mPrefHome;

    TextView textTemperature;
    TextView dataTemperature;
    Button btnRefresh;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(THEME);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mPrefHome                                         = ((CCustomApplication)getApplication()).getSharedHome();

        Log.d(LOG_TAG, "CActivityHome: onCreate()");

        init();
        initToolbar();
    }

    private void init(){
        drawerLayout                                        = findViewById(DRAW_LAYOUT);
        navigationView                                      = findViewById(NAVI_VIEW);
        textTemperature                                     = findViewById(R.id.txt_notify_temperature);

        dataTemperature                                     = findViewById(R.id.dataTemperature);
        dataTemperature.setText(String.valueOf(CHomeSharedPreferences.getTempData()));

        navigationView.setNavigationItemSelectedListener(this);
        if (cServiceMessagingNats == null) {
            cServiceMessagingNats = new CServiceMessagingNats();
        }

        //BroadcastReceiverConnectivity для отслеживания состояния сети Интернет на телефоне
        if (cBroadcastReceiverConnectivity == null){
            cBroadcastReceiverConnectivity = new CBroadcastReceiverConnectivity();
            IntentFilter intentFilterConnectivity           = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(cBroadcastReceiverConnectivity, intentFilterConnectivity);
        }

        //BroadcastReceiverUpdating для обновления данных на странице приложения
        if (brUpdatingItems == null) {
            brUpdatingItems = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        //отображаем результат на экране приложения
                        dataTemperature.setText(String.valueOf(intent.getDoubleExtra(PARAM_DATA, 0.0)));
                        //запоминаем результат в настройках приложения
                        CHomeSharedPreferences.setTempData(Float.valueOf(dataTemperature.getText().toString()));
                        System.out.print("Time: " + System.currentTimeMillis());
                    }
                    catch (Exception ex){
                        System.out.print(ex.toString());
                    }
                }
            };
        }

        btnRefresh                                          = findViewById(REFRESH);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(MAIN_MENU, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if(item.getItemId() == REFRESH) {
            //обновить данные с датчиков системы
            Single.fromCallable(() -> cServiceMessagingNats.refreshData())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<String>() {
                                   @Override
                                   public void onSuccess(String s) {
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                   }
                               }
                    );

        }
          return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case HOMEPAGE:          // Управление переход на главную страницу приложения
                userHomePage();
                break;
            case NOTIFY:            // Управление переход на страницу с настройками уведомлений
                userNotify();
                break;
            case LOGOUT:            // Управление выходом из учетной записи пользователя
                userLogOut();
                break;
            case SETTINGS:          // Управление переходом на страницу с пользовательскими настройками
                userSettings();
            default:
                return super.onOptionsItemSelected(item);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void userLogOut()  {
        CCustomSharedPreference.setLoginData(false);
        unregisterReceiver(cBroadcastReceiverConnectivity);
        unregisterReceiver(brUpdatingItems);
        startActivity(new Intent(this, CActivityStart.class));
        finish();
    }

    public void userHomePage() {
        onBackPressed();
    }

    public void userNotify() {
        startActivity(new Intent(this, CActivityNotifications.class));
        finish();
    }

    public void userSettings() {
        startActivity(new Intent(this,CActivitySettings.class));
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"CActivityHome: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"CActivityHome: onPause()");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dataTemperature.setText(savedInstanceState.getString("dataTemperature"));
        Log.d(LOG_TAG, "CActivityHome: onRestoreInstanceState()");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("dataTemperature",dataTemperature.getText().toString());
        Log.d(LOG_TAG, "CActivityHome: onSaveInstanceState()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilterUpdating = new IntentFilter();
        intentFilterUpdating.addAction(BROADCAST_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(brUpdatingItems,intentFilterUpdating);
        Log.d(LOG_TAG,"CActivityHome: onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "CActivityHome: onRestart()");
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(brUpdatingItems);
        super.onStop();
        Log.d(LOG_TAG,"CActivityHome: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"CActivityHome: onDestroy()");
        //finish();
    }

}