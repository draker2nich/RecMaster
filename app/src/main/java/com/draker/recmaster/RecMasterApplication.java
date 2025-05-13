package com.draker.recmaster;

import android.app.Application;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.draker.recmaster.database.AppDatabase;

/**
 * Кастомный класс Application для инициализации компонентов приложения
 */
public class RecMasterApplication extends Application {
    
    private static final String TAG = "RecMasterApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Инициализируем базу данных при запуске приложения
        AppDatabase.getInstance(this);
        
        // Настраиваем FragmentManager для отображения ошибок
        FragmentManager.enableDebugLogging(true);
        
        // Отслеживаем потребление памяти
        Log.d(TAG, "Приложение запущено. Доступно памяти: " + 
              Runtime.getRuntime().maxMemory() / 1024 / 1024 + " MB");
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "Низкий уровень памяти! Освобождаем ресурсы.");
        
        // Принудительный сборщик мусора
        System.gc();
    }
}