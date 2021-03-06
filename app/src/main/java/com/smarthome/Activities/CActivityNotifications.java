package com.smarthome.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.smarthome.R;

public class CActivityNotifications extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG                                 = "status";
    private static final int LAYOUT                                     = R.layout.activity_notifications;
    private static final int THEME                                      = R.style.AppDefault;
    private static final int DRAW_LAYOUT                                = R.id.drawerLayout;
    private static final int TOOLBAR                                    = R.id.toolbar;
    private static final int HOMEPAGE                                   = R.id.HomePage;
    private static final int SWTTEMP                                    = R.id.switchTemperature;
    private static final int TXTTEMP                                    = R.id.txt_notify_temperature;
    private static final int NAVI_VIEW                                  = R.id.navigation;
    private static final int SETTINGS                                   = R.id.Settings;
    private static final int NOTIFY                                     = R.id.Notify;
    private static final int LOGOUT                                     = R.id.Logout;

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public TextView txtTemp;
    public static Switch swtTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(THEME);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        init();
        initToolbar();

    }

    private void init() {
        drawerLayout                                                    = findViewById(DRAW_LAYOUT);
        navigationView                                                  = findViewById(NAVI_VIEW);
        txtTemp                                                         = findViewById(TXTTEMP);
        swtTemp                                                         = findViewById(SWTTEMP);

        navigationView.setNavigationItemSelectedListener(this);
        swtTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Рассылка уведомлений о получений данных о погоде установлена
                } else {
                    //Рассылка уведомлений о получении данных о погоде установлена
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar                     = findViewById(TOOLBAR);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Notifications");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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


    public void userHomePage() {
        startActivity(new Intent(this, CActivityHome.class));
        finish();
    }

    public void userNotify() {
        onBackPressed();
    }

    public void userLogOut() {
        startActivity(new Intent(this, CActivityStart.class));
        finish();
    }

    public void userSettings() {
        startActivity(new Intent(this, CActivitySettings.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"CActivityNotifications: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"CActivityNotifications: onPause()");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "CActivityNotifications: onRestoreInstanceState()");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "CActivityNotifications: onSaveInstanceState()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"CActivityNotifications: onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "CActivityNotifications: onRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"CActivityNotifications: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"CActivityNotifications: onDestroy()");
    }

}
