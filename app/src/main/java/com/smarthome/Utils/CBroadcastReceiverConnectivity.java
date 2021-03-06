package com.smarthome.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.smarthome.Nats.CServiceMessagingNats;
import java.util.concurrent.TimeUnit;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import static com.smarthome.Nats.CServiceMessagingNats.cServiceMessagingNats;
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
                CServiceMessagingNats.mContext = context;
                Single.fromCallable(() -> cServiceMessagingNats.connect())
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
                Single.fromCallable(() -> cServiceMessagingNats.connect())
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
                Single.fromCallable(() -> cServiceMessagingNats.close())
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
