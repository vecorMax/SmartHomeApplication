package com.smarthome.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.smarthome.R;

public class CSharedPreferences {

    private static final int PRIVATE = Context.MODE_PRIVATE;

    private SharedPreferences sharedPreferences;
    private Context context;

    public CSharedPreferences(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference), PRIVATE);
    }

    public void writeLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_preferences), status);
        editor.commit();
    }

    public boolean readLoginStatus(){
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preferences), false);
        return status;
    }
}
