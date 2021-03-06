package com.smarthome.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.smarthome.R;
import com.smarthome.Utils.CCustomApplication;
import com.smarthome.SharedPreferences.CCustomSharedPreference;
import com.smarthome.SharedPreferences.CSettingsSharedPreferences;

import static com.smarthome.Nats.CServiceMessagingNats.sendMessageToServer;

public class CActivitySettings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG                                 = "status";
    private static final int THEME                                      = R.style.AppDefault;
    private static final int LAYOUT                                     = R.layout.activity_settings;
    private static final int TOOLBAR                                    = R.id.toolbar;
    private static final int DRAW_LAYOUT                                = R.id.drawerLayout;
    private static final int LOGOUT                                     = R.id.Logout;
    private static final int HOMEPAGE                                   = R.id.HomePage;
    private static final int NOTIFY                                     = R.id.Notify;
    private static final int SETTINGS                                   = R.id.Settings;
    private static final int NAVI_VIEW                                  = R.id.navigation;
    private static final int TEMP_DELAY                                 = R.id.enter_temperature_delay;
    private static final int TMP_DL_DGT                                 = R.id.enter_temperature_delay_digit;
    private static final int SAVE_SETTG                                 = R.id.save_user_settings;
    private static final int IVRESCHANG                                 = R.id.imageViewResultChanges;
    private static final int IVEDITABLE                                 = R.id.imageViewEditable;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView mTextViewTemperatureDelay;
    private EditText mEditTextTemperatureDelay;
    private ImageView mImageViewEditable;
    private ImageView mImageViewAcceptChanges;
    private Button mButtonSaveUserSettings;

    protected static CSettingsSharedPreferences mPrefSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "CActivityHome: onCreate()");

        setTheme(THEME);
        setContentView(LAYOUT);

        mPrefSettings                                                   = ((CCustomApplication)getApplication()).getSharedSettings();

        init();
        initToolbar();
    }

    private void init(){
        drawerLayout                                                    = findViewById(DRAW_LAYOUT);
        mImageViewAcceptChanges                                         = findViewById(IVRESCHANG);
        mTextViewTemperatureDelay                                       = findViewById(TEMP_DELAY);

        navigationView                                                  = findViewById(NAVI_VIEW);
        navigationView.setNavigationItemSelectedListener(this);

        mEditTextTemperatureDelay                                       = findViewById(TMP_DL_DGT);
        mEditTextTemperatureDelay.setText(String.valueOf(CSettingsSharedPreferences.getTimeDelayData()));
        mEditTextTemperatureDelay.setTag(mEditTextTemperatureDelay.getKeyListener());
        mEditTextTemperatureDelay.setKeyListener(null); // по умолчанию установлен запрет на изменение данных
        mEditTextTemperatureDelay.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        mImageViewEditable                                              = findViewById(IVEDITABLE);
        mImageViewEditable.setOnClickListener(v -> {
            //по кнопке открываем поле mEditTextTemperatureDelay для редактирования
            mEditTextTemperatureDelay.setKeyListener((KeyListener) mEditTextTemperatureDelay.getTag());
            mEditTextTemperatureDelay.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        });

        mButtonSaveUserSettings                                         = findViewById(SAVE_SETTG);
        mButtonSaveUserSettings.setOnClickListener(v -> {
            // отправляем сообщение на сервер об изменении частоты опроса датчика температуры
            sendMessageToServer(mEditTextTemperatureDelay);
            //запоминаем изменения частоты опроса датчика в настройках мобильного приложения
            CSettingsSharedPreferences.setTimeDelayData(Float.valueOf(mEditTextTemperatureDelay.getText().toString()));
        });
    }

    private void initToolbar() {
        Toolbar toolbar                                                 = findViewById(TOOLBAR);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
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
        startActivity(new Intent(this, CActivityStart.class));
        finish();
    }

    public void userHomePage() {
        startActivity(new Intent(this, CActivityHome.class));
        finish();
    }

    public void userNotify() {
        startActivity(new Intent(this, CActivityNotifications.class));
        finish();
    }

    public void userSettings() {
        onBackPressed();
    }


}
