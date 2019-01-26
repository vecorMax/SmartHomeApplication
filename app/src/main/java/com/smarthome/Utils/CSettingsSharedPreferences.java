package com.smarthome.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Хранилище пользовательских настроек системы
 */
public class CSettingsSharedPreferences
{
    private static SharedPreferences sharedPrefSettings;

    public CSettingsSharedPreferences(Context context){
        sharedPrefSettings = context.getSharedPreferences("SHARED_PREF_SETTINGS", Context.MODE_PRIVATE);
    }

    //Save Time Delay
    public static void setTimeDelayData(Float tempData){
        sharedPrefSettings.edit().putFloat("TIME_DELAY", tempData).apply();
    }

    //Get Time Delay
    public static Float getTimeDelayData(){
        return sharedPrefSettings.getFloat("TIME_DELAY", 0);
    }
}
