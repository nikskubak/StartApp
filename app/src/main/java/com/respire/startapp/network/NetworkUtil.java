package com.respire.startapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


@SuppressWarnings({"WeakerAccess", "unused"})
public class NetworkUtil {

    @SuppressWarnings("unused")
    private static final int TYPE_WIFI = 1;
    @SuppressWarnings("unused")
    private static final int TYPE_MOBILE = 2;
    @SuppressWarnings("unused")
    private static final int  TYPE_NOT_CONNECTED = 0;

    @SuppressWarnings("unused")
    private NetworkUtil() {

    }

    @SuppressWarnings("unused")
    public static NetworkUtil getInstance() {
        return NetworkHolder.INSTANCE;
    }

    @SuppressWarnings("unused")
    public boolean isConnected(final Context context) {
        return checkConnection(((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo());
    }

    @SuppressWarnings("unused")
    private boolean checkConnection(final NetworkInfo networkInfo) {
        return (networkInfo != null && networkInfo.isConnected());
    }

    @SuppressWarnings("unused")
    private static class NetworkHolder {
        @SuppressWarnings("unused")
        private static final NetworkUtil INSTANCE = new NetworkUtil();
    }


    @SuppressWarnings("unused")
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    @SuppressWarnings("unused")
    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
}
