package com.smarthome.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CHomeSharedPreferences
{
    private static SharedPreferences sharedPrefHome;

    public CHomeSharedPreferences(Context context){
        sharedPrefHome = context.getSharedPreferences("SHARED_PREF_HOME", Context.MODE_PRIVATE);
    }

    //Save Temperature Data
    public static void setTempData(Float tempData){
        sharedPrefHome.edit().putFloat("TEMP", tempData).apply();
    }

    //Get Temperature Data
    public static Float getTempData(){
        return sharedPrefHome.getFloat("TEMP", 0);
    }


}
