package com.smarthome.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smarthome.Nats.CServiceMessagingNats;
import com.smarthome.R;
import com.smarthome.Utils.CCustomSharedPreference;

import java.util.Calendar;

import static com.smarthome.Nats.CServiceMessagingNats.cServiceMessagingNats;

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

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView mTextViewTemperatureDelay;
    private EditText mEditTextTemperatureDelay;
    private Button mButtonSaveUserSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "CActivityHome: onCreate()");

        setTheme(THEME);
        setContentView(LAYOUT);

        init();
        initToolbar();
    }

    private void init(){
        drawerLayout                                                    = findViewById(DRAW_LAYOUT);
        navigationView                                                  = findViewById(NAVI_VIEW);
        mTextViewTemperatureDelay                                       = findViewById(TEMP_DELAY);
        mEditTextTemperatureDelay                                       = findViewById(TMP_DL_DGT);
        mButtonSaveUserSettings                                         = findViewById(SAVE_SETTG);
        navigationView.setNavigationItemSelectedListener(this);
        mButtonSaveUserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //по кнопке отправляем сообщение на сервер

                StructData structData                                   = new StructData();
                structData.UUID                                         = "*b827eb05c42d";
                structData.ObjectMeasure                                = "Temperature";
                Calendar calendar                                       = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                structData.CurrentTime                                  = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" +
                                                                          calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                                                                          calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "." +
                                                                          calendar.get(Calendar.MILLISECOND);
                structData.Delay                                        = Double.valueOf(mEditTextTemperatureDelay.getText().toString());


                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String json = gson.toJson(structData);

                Single.fromCallable(() -> cServiceMessagingNats.send(json))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<String>() {
                                       @Override
                                       public void onSuccess(String s) {
                                           //Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           //Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                                       }
                                   }
                        );
            }
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

    class StructData {
        String UUID;
        String ObjectMeasure;
        String CurrentTime;
        Double Delay;
    }
}
