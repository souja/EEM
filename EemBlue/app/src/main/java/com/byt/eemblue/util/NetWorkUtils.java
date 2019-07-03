package com.byt.eemblue.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.byt.eemblue.EApp;
import com.byt.eemblue.R;
import com.souja.lib.utils.MTool;

/**
 * Created by do9-android1 on 2015/11/16.
 */
public class NetWorkUtils {
    /**
     * 判断当前网络是否连接
     *
     * @return true  有网
     * false 没网
     */
    public static boolean isNetworkAvailable() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) EApp.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) EApp.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo.isConnected();
    }


    public static boolean checkNetwork() {
        if (!NetWorkUtils.isNetworkAvailable()) {
            MTool.Toast(EApp.getContext(), R.string.netErrTip);
            return false;
        }
        return true;
    }


}
