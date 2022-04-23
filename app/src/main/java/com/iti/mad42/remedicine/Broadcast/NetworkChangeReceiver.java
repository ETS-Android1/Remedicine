package com.iti.mad42.remedicine.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.iti.mad42.remedicine.Model.pojo.NetworkUtility;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static boolean isConnected = false;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtility.getConnectivityStatusString(context);
        Log.e("mando", "network");
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtility.NETWORK_STATUS_NOT_CONNECTED) {
                Log.e("mando", "no network");
                isConnected =false;
            } else {
                isConnected = true;
                Log.e("mando", "network back");
            }
        }
    }
}
