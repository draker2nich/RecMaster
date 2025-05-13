package com.draker.recmaster.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.draker.recmaster.database.dao.AchievementDao;
import com.draker.recmaster.database.dao.BookDao;
import com.draker.recmaster.database.dao.GameDao;
import com.draker.recmaster.database.dao.MovieDao;
import com.draker.recmaster.database.dao.TvShowDao;
import com.draker.recmaster.database.dao.UserAchievementDao;
import com.draker.recmaster.database.dao.UserDao;
import com.draker.recmaster.database.dao.WatchHistoryDao;
import com.draker.recmaster.database.entity.AchievementEntity;
import com.draker.recmaster.database.entity.BookEntity;
import com.draker.recmaster.database.entity.GameEntity;
import com.draker.recmaster.database.entity.MovieEntity;
import com.draker.recmaster.database.entity.TvShowEntity;
import com.draker.recmaster.database.entity.UserAchievementEntity;
import com.draker.recmaster.database.entity.UserEntity;
import com.draker.recmaster.database.entity.WatchHistoryEntity;
import com.draker.recmaster.model.Achievement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Основной класс базы данных Room для приложения RecMaster
 */
@Database(entities = {
        MovieEntity.class,
        UserEntity.class,
        WatchHistoryEntity.class,
        AchievementEntity.class,
        UserAchievementEntity.class,
        TvShowEntity.class,
        BookEntity.class,
        GameEntity.class
}, version = 3, exportSchema = false)
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
    public abstract AchievementDao achievementDao();
    public abstract UserAchievementDao userAchievementDao();
    public abstract TvShowDao tvShowDao();
    public abstract BookDao bookDao();
    public abstract GameDao gameDao();
    
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
                            .fallbackToDestructiveMigration() // При обновлении схемы пересоздаем БД
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
            
            // Предзаполняем базу данных начальными достижениями
            databaseWriteExecutor.execute(() -> {
                Log.d(TAG, "Pre-populating database with achievements");
                AchievementDao achievementDao = INSTANCE.achievementDao();
                
                // Создаем базовый набор достижений
                List<AchievementEntity> achievements = new ArrayList<>();
                
                // Базовые достижения за просмотр фильмов
                achievements.add(new AchievementEntity(
                        new Achievement("first_movie", "Первый шаг", 
                                "Посмотрите свой первый фильм", 
                                "ic_achievement_first_film", 
                                50, Achievement.AchievementType.WATCH_COUNT, 1)
                ));
                
                achievements.add(new AchievementEntity(
                        new Achievement("film_enthusiast", "Киноэнтузиаст", 
                                "Посмотрите 5 фильмов", 
                                "ic_achievement_film_enthusiast", 
                                100, Achievement.AchievementType.WATCH_COUNT, 5)
                ));
                
                achievements.add(new AchievementEntity(
                        new Achievement("film_buff", "Киноман", 
                                "Посмотрите 20 фильмов", 
                                "ic_achievement_film_buff", 
                                200, Achievement.AchievementType.WATCH_COUNT, 20)
                ));
                
                // Достижения за жанры
                achievements.add(new AchievementEntity(
                        new Achievement("comedy_lover", "Любитель комедий", 
                                "Посмотрите 5 комедий", 
                                "ic_achievement_comedy", 
                                100, Achievement.AchievementType.GENRE_WATCH_COUNT, 5)
                ));
                
                achievements.add(new AchievementEntity(
                        new Achievement("action_fan", "Фанат экшена", 
                                "Посмотрите 5 фильмов в жанре экшн", 
                                "ic_achievement_action", 
                                100, Achievement.AchievementType.GENRE_WATCH_COUNT, 5)
                ));
                
                achievements.add(new AchievementEntity(
                        new Achievement("horror_master", "Мастер ужасов", 
                                "Посмотрите 5 фильмов ужасов", 
                                "ic_achievement_horror", 
                                100, Achievement.AchievementType.GENRE_WATCH_COUNT, 5)
                ));
                
                // Достижения за оценки
                achievements.add(new AchievementEntity(
                        new Achievement("first_rate", "Первая оценка", 
                                "Оцените свой первый фильм", 
                                "ic_achievement_rate", 
                                30, Achievement.AchievementType.RATED_MOVIES, 1)
                ));
                
                achievements.add(new AchievementEntity(
                        new Achievement("critic", "Кинокритик", 
                                "Оцените 10 фильмов", 
                                "ic_achievement_critic", 
                                150, Achievement.AchievementType.RATED_MOVIES, 10)
                ));
                
                // Достижения за уровни
                achievements.add(new AchievementEntity(
                        new Achievement("level_5", "Любитель", 
                                "Достигните 5 уровня", 
                                "ic_achievement_level_5", 
                                0, Achievement.AchievementType.LEVEL_REACHED, 5)
                ));
                
                achievements.add(new AchievementEntity(
                        new Achievement("level_10", "Профессионал", 
                                "Достигните 10 уровня", 
                                "ic_achievement_level_10", 
                                0, Achievement.AchievementType.LEVEL_REACHED, 10)
                ));
                
                // Вставляем достижения в базу данных
                achievementDao.insertAll(achievements);
                Log.d(TAG, "Inserted " + achievements.size() + " achievements");
            });
        }
        
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.d(TAG, "Database opened");
        }
    };
}