package com.sethu.andpopularmoviesstage1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sethugayu on 9/14/16.
 */
public class Utils {
    public static boolean isNetworkAvailable(Context cxt) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
