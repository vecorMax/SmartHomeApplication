package com.smarthome.Nats;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import io.nats.client.Nats;
import static com.smarthome.Activities.CActivityHome.BROADCAST_ACTION;


public class CServiceMessagingNats
{
    //Строковые константы
    private static final String LOG_TAG                                     = "ServiceMessaging";
    private static final String ESTALISH                                    = "Establishing connection to NATS server.";
    private static final String CONNNATS                                    = "Connection to NATS server established.";
    private static final String SUBSCORG                                    = "Organized subscription to messages from NATS server";
    private static final String FAILNATS                                    = "Cannot connect to NATS server.";
    private static final String MSGPBLSH                                    = "Message was published!";
    private static final String FAILPBLS                                    = "Cannot publish message to NATS server.";
    private static final String CLSNATS                                     = "Closing connection to NATS server.";
    private static final String CLSDNATS                                    = "Connection to NATS server closed.";
    private static final String FAILCLSD                                    = "Failed to closing connection. Check connection again";
    private static final String FAILPARS                                    = "Cannot parse NATS message as json object.";
    private static final String WORKNATS                                    = "State NATS: connected";
    private static final String NO_NATS                                     = "There is no connection to NATS server";
    private static final String PUBSUCC                                     = "Message was published on server successfully";

    public static final String PARAM_UPD                                    = "upd"; //обновление данных
    public static final String PARAM_UUID                                   = "uuid"; //ID устройства Raspberry Pi3
    public static final String PARAM_OBJECT                                 = "obj_measure"; //Объект измерения (температура, влажность)
    public static final String PARAM_CURRTIME                               = "current_time"; //Время измерения данных
    public static final String PARAM_MODE                                   = "mode"; //Параметр
    public static final String PARAM_DELAY                                  = "delay"; //Частота измерения
    public static final String PARAM_DATA                                   = "data"; //Данные с датчика

    private static final String address                                     = "nats://192.168.1.103:4222";
    private static Connection nats;
    public static Context mContext;

    public static CServiceMessagingNats cServiceMessagingNats;

    /**
     * Соединение с сервером NATS.
     * Оформление подписки на прием вхордящих сообщений от сервера NATS
     * @return
     */
    public String connect()  {
        String result_connect                           = null;
        if (nats == null || !nats.isConnected())
        {
            Log.d(LOG_TAG, ESTALISH);
            try {
                nats                                    = Nats.connect(address);
                Log.d(LOG_TAG, CONNNATS);
                subscribe();
                Log.d(LOG_TAG, SUBSCORG);
                result_connect                          = CONNNATS;
            }
            catch (IOException e) {
                Log.d(LOG_TAG, FAILNATS);
                e.printStackTrace();
                result_connect                          = FAILNATS;
            }
        }
        else if (nats != null)
            return WORKNATS;

        return result_connect;

    }

    /**
     * Отправка сообщений на сервер NATS.
     * @param str
     */
    public String send(String str) {
        connect();
        if (!nats.isConnected())
            return NO_NATS;

        try {
            nats.publish("TEMP_FROM_DEVICE_TO_SERVER", str.getBytes(StandardCharsets.UTF_8));
            Log.d(LOG_TAG, MSGPBLSH);
            return PUBSUCC;
        } catch (IOException e) {
            Log.d(LOG_TAG, FAILPBLS);
            e.printStackTrace();
            return FAILPBLS;
        }
    }

    /**
     * Завершение соединения с сервером NATS.
     * @return
     */
    public String close(){
        if(nats.isConnected())
            return FAILCLSD;

        Log.d(LOG_TAG, CLSNATS);
        nats.close();
        Log.d(LOG_TAG, CLSDNATS);

        return CLSDNATS;
    }

    /**
     * Подписка на сообщения от сервера NATS по subject.
     * Обработка входящих сообщений от сервера NATS.
     */
    public void subscribe(){
        if(!nats.isConnected())
            return;

        MessageHandler handler = (Message msg) -> {
            Charset charset                             = Charset.forName("UTF-8");
            String str                                  = new String(msg.getData(), charset);
            JSONObject json;
            String id;          //идентификатор платы
            String objMeasure;  //объект измерения (Температура, Влажность..)
            String currTime;    //время исселедования датчика
            String mode;        //для светодиода вкл/выыкл; Нагрев/Охлаждение
            String delay;       //время задержки для повторного опроса датчика
            Double data;        //данные с датчика

            try
            {
                json                                    = new JSONObject(str);
                id                                      = json.getString("UUID");
                objMeasure                              = json.getString("ObjectMeasure");
                currTime                                = json.getString("CurrentTime");
                mode                                    = json.getString("Mode");
                delay                                   = json.getString("Delay");
                data                                    = Double.valueOf(json.getString("Data"));

                //отправляем вышеуказанные параметры на обработку и на обновление данных на экране Activity через Intent и BroadcastReceiver
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(PARAM_UPD, 1);
                intent.putExtra(PARAM_UUID, id);
                intent.putExtra(PARAM_OBJECT, objMeasure);
                intent.putExtra(PARAM_CURRTIME, currTime);
                intent.putExtra(PARAM_MODE, mode);
                intent.putExtra(PARAM_DELAY, delay);
                intent.putExtra(PARAM_DATA, data);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
            catch(JSONException e)
            {
                Log.d(LOG_TAG, FAILPARS);
            }

        };

        nats.subscribe("TEMP_IN_DEVICE_FROM_SERVER", handler); //получение данных о текущей температуре с сервера NATS "главной" платы

    }

}



