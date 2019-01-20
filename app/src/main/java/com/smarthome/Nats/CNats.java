package com.smarthome.Nats;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.smarthome.Notifications.CNotifications;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;
import io.nats.client.SyncSubscription;

//public class CNats extends Thread {
//
//    private static final String address = "nats://192.168.1.103:4222";
//    public static CNats mNats;
//    public static Connection natsConnection;
//    private static SyncSubscription natsSubs;
//    public static Context mContext;
//
//    public CNats(Context context){
//        this.mContext = context;
//    }
//
//    @Override
//    public void run() {
//        try {
//            if (natsConnection == null || natsConnection.getState().toString().equals("CLOSED")) {
//
//                //установление соединения с сервером NATS на плате RPi3
//                natsConnection = Nats.connect(address);
//
//                //подписываемся на получение сообщений
//                natsSubs = natsConnection.subscribeSync("TEMP");
//            }
//
//            // получение сообщений от сервера NATS на плате RPi3 в бесконечном цикле
//            subscribe();
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void subscribe() throws IOException, InterruptedException {
//        while (true) {
//
//            Message message1 = natsSubs.nextMessage(0);
//
//            if (message1 != null)
//            {
//                String str = new String(message1.getData(), StandardCharsets.UTF_8);
//                System.out.print(str);
//                CNotifications.createNotification(str,"Temperature in room 1", mContext);
//            }
//        }
//    }
//
//
//    public static void startNATS(Context context) {
//        if (mNats == null || mNats.getState() == Thread.State.TERMINATED) {
//            mNats = new CNats(context);
//            mNats.start();
//        }
//    }
//
//
//    public static void closeNATS(){
//        if (mNats != null || mNats.getState() == Thread.State.RUNNABLE){
//            try {
//                natsConnection = null;
//                mNats.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}




