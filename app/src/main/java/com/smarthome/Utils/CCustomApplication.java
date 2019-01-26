package com.smarthome.Utils;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CCustomApplication extends Application
{
    private Gson gson;
    private GsonBuilder builder;

    private CCustomSharedPreference sharedCustom;
    private CHomeSharedPreferences  sharedHome;

    @Override
    public void onCreate() {
        super.onCreate();
        builder = new GsonBuilder();
        gson = builder.create();
        sharedCustom = new CCustomSharedPreference(getApplicationContext());
        sharedHome = new CHomeSharedPreferences(getApplicationContext());
    }

    public CCustomSharedPreference getSharedCustom(){
        return sharedCustom;
    }

    public CHomeSharedPreferences getSharedHome(){
        return sharedHome;
    }

    public Gson getGsonObject(){
        return gson;
    }

}
