package com.qjm3662.cloud_u_pan.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Tool.NetworkUtils;

/**
 * Created by qjm3662 on 2016/9/29 0029.
 */

public class NetworkReceiver extends BroadcastReceiver{

    private NetworkInfo info_mobile;
    public NetworkReceiver(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "intent============>>>>" + intent.toString());
        App.NeworkFlag = NetworkUtils.getNetworkFlag(context);
        Log.i("TAG", "flag============>>>>" + App.NeworkFlag);

    }
}
