package com.draker.recmaster.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Утилитарный класс для работы с SharedPreferences
 */
public class PreferenceManager {
    
    private static final String PREF_NAME = "RecMasterPrefs";
    private static final String KEY_CURRENT_USER = "current_user";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    
    private final SharedPreferences preferences;
    
    public PreferenceManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * Сохраняет информацию о входе пользователя
     */
    public void saveLoginState(String username, boolean isLoggedIn) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CURRENT_USER, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    
    /**
     * Получает имя текущего пользователя
     */
    public String getCurrentUsername() {
        return preferences.getString(KEY_CURRENT_USER, "");
    }
    
    /**
     * Проверяет, вошел ли пользователь в систему
     */
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * Выход пользователя из системы
     */
    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }
}