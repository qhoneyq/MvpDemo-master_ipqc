package youdian.apk.ipqc.utils;

import android.content.Context;
import android.net.ConnectivityManager;


import youdian.apk.ipqc.base.Baseapplicton;

/**
 * Created by jiang on 2017/2/23.
 */

public class OSUtil {
    public static boolean hasInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Baseapplicton.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = connectivityManager.getActiveNetworkInfo() != null;
        return isConnected;
    }
}
