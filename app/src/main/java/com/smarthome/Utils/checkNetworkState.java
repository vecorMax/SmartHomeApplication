package com.smarthome.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class checkNetworkState
{
    private static final int TYPE_NOT_CONNECTED = 0;
    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int NETWORK_STATUS_NOT_CONNECTED = 0;
    private static final int NETWORK_STATUS_WIFI = 1;
    private static final int NETWORK_STATUS_MOBILE = 2;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = checkNetworkState.getConnectivityStatus(context);
        int status = 0;
        if (conn == checkNetworkState.TYPE_WIFI) {
            status = NETWORK_STATUS_WIFI;
        } else if (conn == checkNetworkState.TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        } else if (conn == checkNetworkState.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }
}
