package com.smarthome.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.smarthome.R;
import com.smarthome.Utils.CCustomApplication;
import com.smarthome.SharedPreferences.CCustomSharedPreference;

import static com.smarthome.Activities.CActivityLogin.mPrefCustom;

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

        mPrefCustom                                         = ((CCustomApplication)getApplication()).getSharedCustom();

        ActionBar actionBar                                 = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        Button signInButton                                 = findViewById(SIGNIN);
        signInButton.setOnClickListener(view -> {
            Intent loginIntent                              = new Intent(CActivityStart.this, CActivityLogin.class);
            startActivity(loginIntent);
        });

        Button signUpButton                                 = findViewById(SIGNUP);
        signUpButton.setOnClickListener(view -> {
            Intent signInIntent                             = new Intent(CActivityStart.this, CActivitySignUp.class);
            startActivity(signInIntent);
        });

        if (CCustomSharedPreference.getLoginData()){
            Intent userIntent                               = new Intent(CActivityStart.this, CActivityHome.class);
            startActivity(userIntent);
            finish();
        }
    }
}
