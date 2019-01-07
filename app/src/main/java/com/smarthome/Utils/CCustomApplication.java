package com.smarthome.Utils;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CCustomApplication extends Application
{
    private Gson gson;
    private GsonBuilder builder;

    private CCustomSharedPreference shared;

    @Override
    public void onCreate() {
        super.onCreate();
        builder = new GsonBuilder();
        gson = builder.create();
        shared = new CCustomSharedPreference(getApplicationContext());
    }

    public CCustomSharedPreference getShared(){
        return shared;
    }

    public Gson getGsonObject(){
        return gson;
    }

}
