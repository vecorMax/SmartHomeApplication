package com.smarthome.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.smarthome.R;
import com.smarthome.Utils.CBroadcastReceiver;
import com.smarthome.Utils.CCustomApplication;
import com.smarthome.Utils.CCustomSharedPreference;
import com.smarthome.Utils.CServiceTools;

import static com.smarthome.Activities.CActivityLogin.mPref;

public class CActivityStart extends AppCompatActivity {

    private static final String TAG                         = CActivityStart.class.getSimpleName();
    private static final String LOG_TAG                     = "status";
    private static final int LAYOUT                         = R.layout.activity_start;
    private static final int SIGNIN                         = R.id.sign_in;
    private static final int SIGNUP                         = R.id.sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPref                                               = ((CCustomApplication)getApplication()).getShared();

        ActionBar actionBar                                 = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        Button signInButton                                 = findViewById(SIGNIN);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent                          = new Intent(CActivityStart.this, CActivityLogin.class);
                startActivity(loginIntent);
            }
        });

        Button signUpButton                                 = findViewById(SIGNUP);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent                         = new Intent(CActivityStart.this, CActivitySignUp.class);
                startActivity(signInIntent);
            }
        });


        if (CCustomSharedPreference.getLoginData()){
            Intent userIntent                               = new Intent(CActivityStart.this, CActivityMain.class);
            startActivity(userIntent);
            finish();
        }
    }


}
