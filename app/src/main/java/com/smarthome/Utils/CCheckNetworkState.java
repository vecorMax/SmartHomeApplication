package com.smarthome.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CCheckNetworkState
{
    static final int TYPE_NOT_CONNECTED                         = 0;
    static final int TYPE_WIFI                                  = 1;
    static final int TYPE_MOBILE                                = 2;
    static final int NETWORK_STATUS_NOT_CONNECTED               = 0;
    static final int NETWORK_STATUS_WIFI                        = 1;
    static final int NETWORK_STATUS_MOBILE                      = 2;

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
        int conn = CCheckNetworkState.getConnectivityStatus(context);
        if (conn == CCheckNetworkState.TYPE_WIFI) {
            return NETWORK_STATUS_WIFI;
        } else if (conn == CCheckNetworkState.TYPE_MOBILE) {
            return NETWORK_STATUS_MOBILE;
        }
        return NETWORK_STATUS_NOT_CONNECTED;
    }
}
