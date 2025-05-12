package com.draker.recmaster.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

/**
 * Утилитарный класс для проверки состояния сети
 */
public class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    /**
     * Проверяет, есть ли у устройства доступ к сети
     * @param context Контекст приложения
     * @return true если есть подключение к сети, иначе false
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        
        ConnectivityManager connectivityManager = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null) {
            return false;
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = connectivityManager
                    .getNetworkCapabilities(connectivityManager.getActiveNetwork());
            
            if (capabilities != null) {
                boolean hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                Log.d(TAG, "Network available: " + hasInternet);
                return hasInternet;
            }
        } else {
            // Для устройств с API ниже 23
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            boolean hasInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();
            Log.d(TAG, "Network available (legacy): " + hasInternet);
            return hasInternet;
        }
        
        Log.d(TAG, "Network not available");
        return false;
    }
}