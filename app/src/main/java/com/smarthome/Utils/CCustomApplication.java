package com.smarthome.Utils;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smarthome.SharedPreferences.CCustomSharedPreference;
import com.smarthome.SharedPreferences.CHomeSharedPreferences;
import com.smarthome.SharedPreferences.CSettingsSharedPreferences;

public class CCustomApplication extends Application
{
    private Gson gson;
    private GsonBuilder builder;

    private CCustomSharedPreference sharedCustom;
    private CHomeSharedPreferences sharedHome;
    private CSettingsSharedPreferences sharedSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        builder                                                                     = new GsonBuilder();
        gson                                                                        = builder.create();
        sharedCustom                                                                = new CCustomSharedPreference(getApplicationContext());
        sharedHome                                                                  = new CHomeSharedPreferences(getApplicationContext());
        sharedSettings                                                              = new CSettingsSharedPreferences(getApplicationContext());
    }

    public CCustomSharedPreference getSharedCustom(){
        return sharedCustom;
    }

    public CHomeSharedPreferences getSharedHome(){
        return sharedHome;
    }

    public CSettingsSharedPreferences getSharedSettings() {
        return sharedSettings;
    }

    public Gson getGsonObject(){
        return gson;
    }

}
