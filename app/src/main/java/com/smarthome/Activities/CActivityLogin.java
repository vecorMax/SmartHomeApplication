package com.smarthome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smarthome.R;
import com.smarthome.Utils.CSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import static com.smarthome.R.string.login_preference;
import static com.smarthome.R.string.login_status_preferences;

public class CActivityLogin extends AppCompatActivity {

    private static final int LAYOUT                         = R.layout.activity_login;
    private static final int USERNAME                       = R.id.etUserName;
    private static final int PASSWORD                       = R.id.etPassword;
    private static final int ENTRY                          = R.id.btnEntry;

    private CSharedPreferences cSharedPreferences;

    Button btn;
    EditText usr, psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        cSharedPreferences                                  = new CSharedPreferences(getApplicationContext(), login_preference);

        init();

        if (cSharedPreferences.readData(login_status_preferences)) {
            startActivity(new Intent(this, CActivityMain.class));
            finish();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usr.getText().toString().equals("Admin") && psd.getText().toString().equals("Admin")) {
                    Intent mainIntent                       = new Intent(CActivityLogin.this, CActivityMain.class);
                    startActivity(mainIntent);
                    cSharedPreferences.writeData(true, login_status_preferences);
                    finish();
                }
            }
        });
    }

    /****************************************************************************************************
     * Инициализация элементов CActivityLogin                                                           *
     * @param                                                                                           *
     * @return                                                                                          *
     ***************************************************************************************************/
    public void init() {
        usr                                                 = findViewById(USERNAME);
        psd                                                 = findViewById(PASSWORD);
        btn                                                 = findViewById(ENTRY);
    }
}


