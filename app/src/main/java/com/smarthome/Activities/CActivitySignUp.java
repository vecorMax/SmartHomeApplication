package com.smarthome.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smarthome.R;
import com.smarthome.Utils.CCustomApplication;
import com.smarthome.Utils.CCustomSharedPreference;
import com.smarthome.Utils.CUserObject;

public class CActivitySignUp extends AppCompatActivity {

    private static final String TAG                         = CActivitySignUp.class.getSimpleName();
    private static final int LAYOUT                         = R.layout.activity_sign_up;
    private static final int USRNAM                         = R.id.username;
    private static final int EMAIL                          = R.id.email;
    private static final int PSSWRD                         = R.id.password;
    private static final int ADDRSS                         = R.id.address;
    private static final int PHNNUM                         = R.id.phone_number;
    private static final int RADGRP                         = R.id.radio_group;
    private static final int FNGPRN                         = R.id.with_fingerprint;
    private static final int FNGPSW                         = R.id.with_fingerprint_and_password;
    private static final int SIGNUP                         = R.id.sign_up_button;

    private TextView displayError;

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText address;
    private EditText phoneNumber;

    private RadioGroup radioGroup;

    private boolean loginOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        setTitle("Android Fingerprint Registration");

        username                                            = findViewById(USRNAM);
        email                                               = findViewById(EMAIL);
        password                                            = findViewById(PSSWRD);
        address                                             = findViewById(ADDRSS);
        phoneNumber                                         = findViewById(PHNNUM);

        radioGroup                                          = findViewById(RADGRP);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id == FNGPRN){
                    loginOption                             = false;
                }
                if(id == FNGPSW){
                    loginOption                             = true;
                }
            }
        });

        Button signUpButton                                 = findViewById(SIGNUP);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameValue                        = username.getText().toString();
                String emailValue                           = email.getText().toString();
                String passwordValue                        = password.getText().toString();
                String addressValue                         = address.getText().toString();
                String phonenumberValue                     = phoneNumber.getText().toString();

                int selectedButtonId                        = radioGroup.getCheckedRadioButtonId();

                if(TextUtils.isEmpty(usernameValue) || TextUtils.isEmpty(emailValue)|| TextUtils.isEmpty(passwordValue)
                        || TextUtils.isEmpty(addressValue) || TextUtils.isEmpty(phonenumberValue)){
                    Toast.makeText(CActivitySignUp.this, "All input fields must be filled", Toast.LENGTH_LONG).show();
                }else if(selectedButtonId == -1){
                    Toast.makeText(CActivitySignUp.this, "Login option must be selected", Toast.LENGTH_LONG).show();
                }else{
                    Gson gson                               = ((CCustomApplication)getApplication()).getGsonObject();
                    CUserObject userData                    = new CUserObject(usernameValue, emailValue, passwordValue, addressValue, phonenumberValue, loginOption);
                    String userDataString                   = gson.toJson(userData);
                    CCustomSharedPreference pref            = ((CCustomApplication)getApplication()).getShared();
                    pref.setUserData(userDataString);

                    username.setText("");
                    email.setText("");
                    password.setText("");
                    address.setText("");
                    phoneNumber.setText("");

                    Intent loginIntent                      = new Intent(CActivitySignUp.this, CActivityLogin.class);
                    startActivity(loginIntent);
                }
            }
        });
    }
}
