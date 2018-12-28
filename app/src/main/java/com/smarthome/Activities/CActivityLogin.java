package com.smarthome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smarthome.R;

import androidx.appcompat.app.AppCompatActivity;

public class CActivityLogin extends AppCompatActivity {

    private static final int LAYOUT                     = R.layout.activity_login;
    private static final int USERNAME                   = R.id.etUserName;
    private static final int PASSWORD                   = R.id.etPassword;
    private static final int ENTRY                      = R.id.btnEntry;

    Button btn;
    EditText usr, psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        usr                                             = findViewById(USERNAME);
        psd                                             = findViewById(PASSWORD);
        btn                                             = findViewById(ENTRY);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usr.getText().toString().equals("Admin") && psd.getText().toString().equals("Admin"))
                {
                    Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG);
                    Intent mainIntent = new Intent(CActivityLogin.this, CActivityMain.class);
                    startActivity(mainIntent);
                }
            }
        });
    }
}
