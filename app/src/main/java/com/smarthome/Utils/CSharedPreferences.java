package com.smarthome.Utils;

import android.content.Context;
import android.content.SharedPreferences;

//public class CSharedPreferences {
//
//    private static final int PRIVATE = Context.MODE_PRIVATE;
//
//    private SharedPreferences sharedPreferences;
//    private Context context;
//
//    public CSharedPreferences(Context context, int param)
//    {
//        this.context = context;
//        sharedPreferences = context.getSharedPreferences(context.getResources().getString(param), PRIVATE);
//    }
//
//    public void writeData(boolean status, int param){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(context.getResources().getString(param), status).apply();
//    }
//
//    public boolean readData(int param){
//        boolean status = false;
//        status = sharedPreferences.getBoolean(context.getResources().getString(param), false);
//        return status;
//    }
//}
