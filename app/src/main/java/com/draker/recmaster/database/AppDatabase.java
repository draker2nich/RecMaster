package com.draker.recmaster.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.draker.recmaster.database.dao.MovieDao;
import com.draker.recmaster.database.dao.UserDao;
import com.draker.recmaster.database.dao.WatchHistoryDao;
import com.draker.recmaster.database.entity.MovieEntity;
import com.draker.recmaster.database.entity.UserEntity;
import com.draker.recmaster.database.entity.WatchHistoryEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Основной класс базы данных Room для приложения RecMaster
 */
@Database(entities = {MovieEntity.class, UserEntity.class, WatchHistoryEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String TAG = "AppDatabase";
    private static final String DATABASE_NAME = "recmaster_db";
    
    // Создаем пул потоков для выполнения операций с базой данных в фоне
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    // Синглтон для доступа к базе данных
    private static volatile AppDatabase INSTANCE;
    
    // DAO методы
    public abstract MovieDao movieDao();
    public abstract UserDao userDao();
    public abstract WatchHistoryDao watchHistoryDao();
    
    /**
     * Получение экземпляра базы данных
     */
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    Log.d(TAG, "Creating new database instance");
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        Log.d(TAG, "Getting the database instance");
        return INSTANCE;
    }
    
    /**
     * Callback для инициализации базы данных при её создании
     */
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            
            Log.d(TAG, "Database created");
            
            // Если нужно предзаполнить базу данных начальными данными,
            // добавляем это здесь
            databaseWriteExecutor.execute(() -> {
                // Например, предзаполнение базы данных начальным пользователем
                // или категориями/жанрами
                Log.d(TAG, "Pre-populating database");
            });
        }
        
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.d(TAG, "Database opened");
        }
    };
}