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

    Button btn;
    EditText usr, psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usr = findViewById(R.id.etUserName);
        psd = findViewById(R.id.etPassword);
        btn = findViewById(R.id.btnEntry);

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
