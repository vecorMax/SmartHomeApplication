package com.smarthome.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CCustomSharedPreference
{
    private static SharedPreferences sharedPref;

    public CCustomSharedPreference(Context context) {
        sharedPref = context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
    }


    public SharedPreferences getInstanceOfSharedPreference(){
        return sharedPref;
    }

    //Save user information
    public static void setUserData(String userData){
        sharedPref.edit().putString("USER", userData).apply();
    }

    //Get user information
    public static String getUserData(){
        return sharedPref.getString("USER", "");
    }

    //Save login information
    public static void setLoginData(Boolean loginData){
        sharedPref.edit().putBoolean("LOGIN",loginData).apply();
    }

    //Get login information
    public static Boolean getLoginData(){
        boolean status = false;
        status = sharedPref.getBoolean("LOGIN", false);
        return status;
    }
}
