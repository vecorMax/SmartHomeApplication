package com.smarthome.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * CCustomSharedPrefernces - хранилище пользовательских настроек
 */
public class CCustomSharedPreference
{
    private static SharedPreferences sharedPrefCustom;

    public CCustomSharedPreference(Context context) {
        sharedPrefCustom = context.getSharedPreferences("SHARED_PREF_CUSTOM", Context.MODE_PRIVATE);
    }


//    public SharedPreferences getInstanceOfSharedPreference(){
//        return sharedPrefCustom;
//    }

    //Save user information
    //public static ...
    public void setUserData(String userData){
        sharedPrefCustom.edit().putString("USER", userData).apply();
    }

    //Get user information
    public static String getUserData(){
        return sharedPrefCustom.getString("USER", "");
    }

    //Save login information
    public static void setLoginData(Boolean loginData){
        sharedPrefCustom.edit().putBoolean("LOGIN",loginData).apply();
    }

    //Get login information
    public static Boolean getLoginData(){
        boolean status = false;
        status = sharedPrefCustom.getBoolean("LOGIN", false);
        return status;
    }
}
