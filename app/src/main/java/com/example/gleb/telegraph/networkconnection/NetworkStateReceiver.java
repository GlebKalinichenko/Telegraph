package com.example.gleb.telegraph.networkconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.example.gleb.telegraph.networkconnection.NetworkStateChanged;

import de.greenrobot.event.EventBus;

/**
 * Created by gleb on 16.01.16.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    public static Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mContext == null) mContext = context;
        final WifiManager wm = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
        if (!intent.getBooleanExtra(wm.EXTRA_SUPPLICANT_CONNECTED, true)) {
            EventBus.getDefault().post(new NetworkStateChanged(false));
        }
    }
}
