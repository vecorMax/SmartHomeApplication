package com.smarthome.Nats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.smarthome.Activities.CActivityMain;
import com.smarthome.Utils.CBroadcastReceiverConnectivity;
import com.smarthome.Utils.CBroadcastReceiverUpdating;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import io.nats.client.Nats;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.smarthome.Activities.CActivityMain.cBroadcastReceiverUpdating;

public class CServiceMessaging
{
    //Строковые константы
    private static final String LOG_TAG                 = "ServiceMessaging";
    private static final String ESTALISH                = "Establishing connection to NATS server.";
    private static final String CONNNATS                = "Connection to NATS server established.";
    private static final String SUBSCORG                = "Organized subscription to messages from NATS server";
    private static final String FAILNATS                = "Cannot connect to NATS server.";
    private static final String MSGPBLSH                = "Message was published!";
    private static final String FAILPBLS                = "Cannot publish message to NATS server.";
    private static final String CLSNATS                 = "Closing connection to NATS server.";
    private static final String CLSDNATS                = "Connection to NATS server closed.";
    private static final String FAILCLSD                = "Failed to closing connection. Check connection again";
    private static final String FAILPARS                = "Cannot parse NATS message as json object.";
    private static final String WORKNATS                = "State NATS: connected";

    private static final String address                 = "nats://192.168.1.103:4222";
    private static Connection nats;
    public static Context mContext;

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

    public void send(Message msg) {
        connect();
        if (!nats.isConnected())
            return;

        try {
            nats.publish("TEMP", msg.getData());
            Log.d(LOG_TAG, MSGPBLSH);
        } catch (IOException e) {
            Log.d(LOG_TAG, FAILPBLS);
            e.printStackTrace();
        }
    }

    public String close(){
        if(nats.isConnected())
            return FAILCLSD;

        Log.d(LOG_TAG, CLSNATS);
        nats.close();
        Log.d(LOG_TAG, CLSDNATS);

        return CLSDNATS;
    }

    public void subscribe(){
        if(!nats.isConnected())
            return;

        MessageHandler handler = (Message msg) -> {
            Charset charset                             = Charset.forName("UTF-8");
            String str                                  = new String(msg.getData(), charset);
            JSONObject json;
            String id;            //идентификатор платы
            String objMeasure;  //объект измерения (Температура, Влажность..)
            String currTime;    //время исселедования датчика
            String mode;        //для светодиода вкл/выыкл; Нагрев/Охлаждение
            String delay;       //время задержки для повторного опроса датчика
            String data;        //данные с датчика

            try
            {
                json                                    = new JSONObject(str);
                id                                      = json.getString("UUID");
                objMeasure                              = json.getString("Object Measure");
                currTime                                = json.getString("Current Time");
                mode                                    = json.getString("Mode");
                delay                                   = json.getString("Sleep");
                data                                    = json.getString("Data");

                //отправляем вышеуказанные параметры на обработку и на обновление данных на экране Activity через Intent и BroadcastReceiver
                Intent intent = new Intent(mContext, CBroadcastReceiverConnectivity.class);
                intent.putExtra(CBroadcastReceiverUpdating.PARAM_UPD, 1);
                //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                intent.putExtra(CBroadcastReceiverUpdating.PARAM_UUID, id);
                intent.putExtra(CBroadcastReceiverUpdating.PARAM_OBJECT, objMeasure);
                intent.putExtra(CBroadcastReceiverUpdating.PARAM_CURRTIME, currTime);
                intent.putExtra(CBroadcastReceiverUpdating.PARAM_MODE, mode);
                intent.putExtra(CBroadcastReceiverUpdating.PARAM_DELAY, delay);
                intent.putExtra(CBroadcastReceiverUpdating.PARAM_DATA, data);
                //sendBroadcast(intent);
                cBroadcastReceiverUpdating.onReceive(mContext,intent);

            }
            catch(JSONException e)
            {
                Log.d(LOG_TAG, FAILPARS);
            }

        };

        nats.subscribe("TEMP", handler); //получение данных о текущей температуре с сервера NATS "главной" платы

    }

}




