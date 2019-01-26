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


}
