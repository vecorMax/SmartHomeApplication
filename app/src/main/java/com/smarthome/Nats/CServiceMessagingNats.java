package com.smarthome.Nats;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Calendar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import io.nats.client.Nats;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

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
    private static final String REFRDATA                                    = "Data was updated successfully!";

    public static final String PARAM_UPD                                    = "upd"; //обновление данных
    public static final String PARAM_UUID                                   = "uuid"; //ID устройства Raspberry Pi3
    public static final String PARAM_OBJECT                                 = "obj_measure"; //Объект измерения (температура, влажность)
    public static final String PARAM_CURRTIME                               = "current_time"; //Время измерения данных
    public static final String PARAM_MODE                                   = "mode"; //Параметр
    public static final String PARAM_DELAY                                  = "delay"; //Частота измерения
    public static final String PARAM_DATA                                   = "data"; //Данные с датчика

    private static final String MAIN_SERVER                                 = "*b827eb05c42d";

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
        String result_connect                                               = null;
        if (nats == null || !nats.isConnected())
        {
            Log.d(LOG_TAG, ESTALISH);
            try {
                nats                                                        = Nats.connect(address);
                Log.d(LOG_TAG, CONNNATS);
                refreshData();
                Log.d(LOG_TAG, REFRDATA);
                subscribe();
                Log.d(LOG_TAG, SUBSCORG);
                result_connect                                              = CONNNATS;
            }
            catch (IOException e) {
                Log.d(LOG_TAG, FAILNATS);
                e.printStackTrace();
                result_connect                                              = FAILNATS;
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
    private String send(String str) {
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
     * Подписка на сообщения от сервера NATS по subject при изменениях текущей температуры
     * Обработка входящих сообщений от сервера NATS.
     */
    private void subscribe(){
        if(!nats.isConnected())
            return;

        MessageHandler handler = (Message msg) -> {
            Charset charset                                                 = Charset.forName("UTF-8");
            String str                                                      = new String(msg.getData(), charset);
            JSONObject json;
            String id;          //идентификатор платы
            String objMeasure;  //объект измерения (Температура, Влажность..)
            String currTime;    //время исселедования датчика
            String mode;        //для светодиода вкл/выыкл; Нагрев/Охлаждение
            String delay;       //время задержки для повторного опроса датчика
            Double data;        //данные с датчика

            try
            {
                json                                                        = new JSONObject(str);
                id                                                          = json.getString("UUID");
                objMeasure                                                  = json.getString("ObjectMeasure");
                currTime                                                    = json.getString("CurrentTime");
                mode                                                        = json.getString("Mode");
                delay                                                       = json.getString("Delay");
                data                                                        = Double.valueOf(json.getString("Data"));

                //отправляем вышеуказанные параметры на обработку и на обновление данных на экране Activity через Intent и BroadcastReceiver
                Intent intent                                               = new Intent(BROADCAST_ACTION);
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

        nats.subscribe("TEMP_IN_DEVICE_FROM_SERVER", handler);
    }


    /**
     * Отправление запроса на сервер для получения "свежых" данных
     * при входе в приложение и при нажатии кнопки "Обновить" в меню
     * приложения
     */
    public String  refreshData(){
        if(!nats.isConnected())
            return FAILCLSD;

        try {
            Message msg = nats.request("TEMP_IN_DEVICE_FROM_SERVER_UPD","Help me".getBytes(), 10000);
            Charset charset                                                 = Charset.forName("UTF-8");
            String str                                                      = new String(msg.getData(), charset);
            JSONObject json;
            String objMeasure;  //объект измерения (Температура, Влажность..)
            Double data;        //данные с датчика
            try
            {
                json                                                        = new JSONObject(str);
                objMeasure                                                  = json.getString("ObjectMeasure");
                data                                                        = Double.valueOf(json.getString("Data"));

                //отправляем вышеуказанные параметры на обработку и на обновление данных на экране Activity через Intent и BroadcastReceiver
                Intent intent                                               = new Intent(BROADCAST_ACTION);
                intent.putExtra(PARAM_UPD, 1);
                intent.putExtra(PARAM_OBJECT, objMeasure);
                intent.putExtra(PARAM_DATA, data);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                return PUBSUCC;
            }
            catch(JSONException e)
            {
                Log.d(LOG_TAG, FAILPARS);
                return FAILPBLS;
            }


        } catch (IOException e) {
            e.printStackTrace();
            return FAILPBLS;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return FAILPBLS;
        }
    }

    static class StructData {
        String UUID;
        String ObjectMeasure;
        String CurrentTime;
        Double Delay;
    }

    /**
     * Формирование данных для отправки их на сервер NATS
     * @param editText - новое значение частоты опроса датчика температуры
     */
    public static void sendMessageToServer(EditText editText) {
        StructData structData                                               = new StructData();
        structData.UUID                                                     = MAIN_SERVER;
        structData.ObjectMeasure                                            = "Temperature";
        Calendar calendar                                                   = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        structData.CurrentTime                                  = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "." +
                calendar.get(Calendar.MILLISECOND);
        structData.Delay                                                    = Double.valueOf(editText.getText().toString());


        GsonBuilder builder                                                 = new GsonBuilder();
        Gson gson                                                           = builder.create();
        String json                                                         = gson.toJson(structData);

        Single.fromCallable(() -> cServiceMessagingNats.send(json))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<String>() {
                               @Override
                               public void onSuccess(String s) {
                               }

                               @Override
                               public void onError(Throwable e) {
                               }
                           }
                );
    }

}




