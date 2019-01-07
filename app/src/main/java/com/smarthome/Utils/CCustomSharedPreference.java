package com.smarthome.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CCustomSharedPreference
{
    private SharedPreferences sharedPref;

    public CCustomSharedPreference(Context context) {
        sharedPref = context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
    }

    public SharedPreferences getInstanceOfSharedPreference(){
        return sharedPref;
    }

    //Save user information
    public void setUserData(String userData){
        sharedPref.edit().putString("USER", userData).apply();
    }

    public String getUserData(){
        return sharedPref.getString("USER", "");
    }
}
