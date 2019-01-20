package com.smarthome.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.smarthome.Activities.CActivityMain;

import java.util.UUID;

import static com.smarthome.Activities.CActivityMain.h;

public class CBroadcastReceiverUpdating extends BroadcastReceiver {

    public static String PARAM_UPD                                  = "upd"; //обновление данных
    public static String PARAM_UUID                                 = "uuid"; //ID устройства Raspberry Pi3
    public static String PARAM_OBJECT                               = "obj_measure"; //Объект измерения (температура, влажность)
    public static String PARAM_CURRTIME                             = "current_time"; //Время измерения данных
    public static String PARAM_MODE                                 = "mode"; //Параметр
    public static String PARAM_DELAY                                = "delay"; //Частота измерения
    public static String PARAM_DATA                                 = "data"; //Данные с датчика


    @Override
    public void onReceive(Context context, Intent intent) {
        //Получение данных по параметрам
        int status                                                  = intent.getIntExtra(PARAM_UPD, 0);
        if (status == 1) {
            UUID uuid                                               = UUID.fromString(intent.getStringExtra(PARAM_UUID));
            String obj_measure                                      = intent.getStringExtra(PARAM_OBJECT);
            String current_time                                     = intent.getStringExtra(PARAM_CURRTIME);
            String mode                                             = intent.getStringExtra(PARAM_MODE);
            String delay                                            = intent.getStringExtra(PARAM_DELAY);
            String data                                             = intent.getStringExtra(PARAM_DATA);

            //По простому обновим показание температуры
            //+ Должна быть установлена првоерка, чтобы пользователь получил уведомление на телефон
            //CActivityMain.dataTemperature.setText(data.toString());
            Bundle dataBundle = new Bundle();
            dataBundle.putString(PARAM_OBJECT, obj_measure);
            dataBundle.putString(PARAM_DATA, data);
            Message msg = new Message();
            msg.setData(dataBundle);
            h.sendMessage(msg);

        }
    }
}
