package com.smarthome.Utils;

import android.accounts.NetworkErrorException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Toast;

import com.smarthome.Activities.CActivityMain;
import com.smarthome.Nats.CNats;
import com.smarthome.Nats.CServiceMessaging;
import com.smarthome.Notifications.CNotifications;

import java.io.IOException;
import java.security.Signature;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.smarthome.Activities.CActivityMain.cServiceMessaging;
import static com.smarthome.Utils.CCheckNetworkState.TYPE_MOBILE;
import static com.smarthome.Utils.CCheckNetworkState.TYPE_NOT_CONNECTED;
import static com.smarthome.Utils.CCheckNetworkState.TYPE_WIFI;

public class CBroadcastReceiverConnectivity extends BroadcastReceiver
{
    private static final String LOG_TAG                     = "status";

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = CCheckNetworkState.getConnectivityStatus(context);
        switch (status) {
            case TYPE_WIFI: //наличие сети Интернет Wi-Fi
                try {
                    //задержка на 15 секунд, чтобы прогрузилась RaspberryPi3
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CServiceMessaging.mContext = context;
                Single.fromCallable(() -> cServiceMessaging.connect())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<String>() {
                                       @Override
                                       public void onSuccess(String s) {
                                          //Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           //Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                                       }
                                   }
                        );
                break;
            case TYPE_MOBILE: //наличие мобильной сети Интернет
                Single.fromCallable(() -> cServiceMessaging.connect())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<String>() {
                                       @Override
                                       public void onSuccess(String s) {
                                           //Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           //Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                                       }
                                   }
                        );
                break;
            case TYPE_NOT_CONNECTED: //отсутствие сети Интернет
                Single.fromCallable(() -> cServiceMessaging.close())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<String>() {
                                       @Override
                                       public void onSuccess(String s) {
                                           //Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           //Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                                       }
                                   }
                        );
                break;
        }
    }
}
