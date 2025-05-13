package com.draker.recmaster.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.MainActivity;
import com.draker.recmaster.R;
import com.draker.recmaster.database.AppDatabase;
import com.draker.recmaster.database.dao.AchievementDao;
import com.draker.recmaster.database.dao.UserAchievementDao;
import com.draker.recmaster.database.dao.UserDao;
import com.draker.recmaster.database.dao.WatchHistoryDao;
import com.draker.recmaster.database.entity.AchievementEntity;
import com.draker.recmaster.database.entity.UserAchievementEntity;
import com.draker.recmaster.database.entity.UserEntity;
import com.draker.recmaster.database.entity.WatchHistoryEntity;
import com.draker.recmaster.model.Achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис, отвечающий за все аспекты геймификации в приложении
 */
public class GamificationService {
    private static final String TAG = "GamificationService";
    private static final String NOTIFICATION_CHANNEL_ID = "achievement_notifications";
    private static final int NOTIFICATION_ID_BASE = 1000;
    
    private final Context context;
    private final AppDatabase database;
    private final AchievementDao achievementDao;
    private final UserAchievementDao userAchievementDao;
    private final UserDao userDao;
    private final WatchHistoryDao watchHistoryDao;
    
    // Кэширование информации о жанрах фильмов для проверки достижений
    private final Map<Integer, List<String>> movieGenresCache = new HashMap<>();
    
    // LiveData для обновления UI при получении новых достижений
    private final MutableLiveData<List<Achievement>> newlyUnlockedAchievements = new MutableLiveData<>(new ArrayList<>());
    
    // Синглтон для службы геймификации
    private static GamificationService INSTANCE;
    
    /**
     * Получить экземпляр сервиса геймификации
     */
    public static GamificationService getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GamificationService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GamificationService(context);
                }
            }
        }
        return INSTANCE;
    }
    
    private GamificationService(Context context) {
        this.context = context.getApplicationContext();
        this.database = AppDatabase.getInstance(context);
        this.achievementDao = database.achievementDao();
        this.userAchievementDao = database.userAchievementDao();
        this.userDao = database.userDao();
        this.watchHistoryDao = database.watchHistoryDao();
        
        // Создание канала уведомлений (требуется для Android 8.0+)
        createNotificationChannel();
    }
    
    /**
     * Создаёт канал уведомлений для Android 8.0+
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Достижения",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Уведомления о новых достижениях");
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    /**
     * Начисляет очки и опыт пользователю за просмотр фильма
     * и проверяет достижения
     */
    public void recordMovieWatched(String username, int movieId, String genre) {
        Log.d(TAG, "Recording movie watched: " + movieId + " by user: " + username);
        
        // Выполняем операцию в фоновом потоке
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // 1. Начисляем базовые очки за просмотр (50 очков)
                int experienceGained = 50;
                userDao.addExperience(username, experienceGained);
                
                // 2. Проверяем и обновляем достижения
                updateAchievements(username, movieId, genre);
                
                // 3. Проверяем новые разблокированные достижения для уведомлений
                checkForNewlyUnlockedAchievements(username);
                
            } catch (Exception e) {
                Log.e(TAG, "Error recording movie watch: ", e);
            }
        });
    }
    
    /**
     * Обновляет достижения пользователя после просмотра фильма
     */
    private void updateAchievements(String username, int movieId, String genre) {
        // 1. Общее количество просмотренных фильмов
        int totalWatchedMovies = watchHistoryDao.getWatchHistoryByUsername(username).getValue().size();
        updateWatchCountAchievements(username, totalWatchedMovies);
        
        // 2. Достижения по жанрам
        if (genre != null && !genre.isEmpty()) {
            updateGenreAchievements(username, genre);
        }
        
        // 3. Достижения по рейтингам
        WatchHistoryEntity watchHistory = watchHistoryDao.getWatchHistoryByMovieAndUsername(movieId, username);
        if (watchHistory != null && watchHistory.getUserRating() > 0) {
            updateRatingAchievements(username);
        }
        
        // 4. Достижения по уровням
        UserEntity user = userDao.getUserByUsernameSync(username);
        if (user != null) {
            updateLevelAchievements(username, user.getLevel());
        }
    }
    
    /**
     * Обновляет достижения, связанные с количеством просмотренных фильмов
     */
    private void updateWatchCountAchievements(String username, int totalWatchedMovies) {
        List<AchievementEntity> watchCountAchievements = achievementDao.getAchievementsByTypeSync(
                Achievement.AchievementType.WATCH_COUNT.name());
        
        for (AchievementEntity achievementEntity : watchCountAchievements) {
            userAchievementDao.updateProgress(
                    username,
                    achievementEntity.getId(),
                    totalWatchedMovies,
                    achievementEntity.getThreshold()
            );
        }
    }
    
    /**
     * Обновляет достижения, связанные с просмотром фильмов определенных жанров
     */
    private void updateGenreAchievements(String username, String genre) {
        // Маппинг идентификаторов достижений жанров
        Map<String, String> genreToAchievementId = new HashMap<>();
        genreToAchievementId.put("Comedy", "comedy_lover");
        genreToAchievementId.put("Action", "action_fan");
        genreToAchievementId.put("Horror", "horror_master");
        
        // Проверяем, есть ли достижение для этого жанра
        String achievementId = genreToAchievementId.get(genre);
        if (achievementId != null) {
            // Считаем, сколько фильмов данного жанра просмотрел пользователь
            // Для простоты предположим, что у нас есть такой метод
            int genreMoviesCount = countWatchedMoviesByGenre(username, genre);
            
            // Обновляем прогресс достижения
            AchievementEntity achievementEntity = achievementDao.getAchievementByIdSync(achievementId);
            if (achievementEntity != null) {
                userAchievementDao.updateProgress(
                        username,
                        achievementId,
                        genreMoviesCount,
                        achievementEntity.getThreshold()
                );
            }
        }
    }
    
    /**
     * Считает количество просмотренных фильмов определенного жанра
     * (это упрощенная реализация, в реальном приложении нужно использовать настоящие данные)
     */
    private int countWatchedMoviesByGenre(String username, String genre) {
        // В реальном приложении здесь будет запрос к базе данных
        // для получения количества просмотренных фильмов данного жанра
        return 1; // Заглушка, просто инкрементируем для теста
    }
    
    /**
     * Обновляет достижения, связанные с оценками фильмов
     */
    private void updateRatingAchievements(String username) {
        // Подсчет количества оцененных фильмов
        int ratedMoviesCount = countRatedMovies(username);
        
        List<AchievementEntity> ratingAchievements = achievementDao.getAchievementsByTypeSync(
                Achievement.AchievementType.RATED_MOVIES.name());
        
        for (AchievementEntity achievementEntity : ratingAchievements) {
            userAchievementDao.updateProgress(
                    username,
                    achievementEntity.getId(),
                    ratedMoviesCount,
                    achievementEntity.getThreshold()
            );
        }
    }
    
    /**
     * Считает количество оцененных фильмов
     */
    private int countRatedMovies(String username) {
        // В реальном приложении здесь будет запрос к базе данных
        // для получения количества оцененных фильмов
        return 1; // Заглушка, просто инкрементируем для теста
    }
    
    /**
     * Обновляет достижения, связанные с уровнем пользователя
     */
    private void updateLevelAchievements(String username, int userLevel) {
        List<AchievementEntity> levelAchievements = achievementDao.getAchievementsByTypeSync(
                Achievement.AchievementType.LEVEL_REACHED.name());
        
        for (AchievementEntity achievementEntity : levelAchievements) {
            userAchievementDao.updateProgress(
                    username,
                    achievementEntity.getId(),
                    userLevel,
                    achievementEntity.getThreshold()
            );
        }
    }
    
    /**
     * Проверяет наличие новых разблокированных достижений и отправляет уведомления
     */
    private void checkForNewlyUnlockedAchievements(String username) {
        List<UserAchievementEntity> newlyUnlocked = userAchievementDao.getNewlyUnlockedAchievementsSync(username);
        
        if (newlyUnlocked != null && !newlyUnlocked.isEmpty()) {
            List<Achievement> unlockedAchievements = new ArrayList<>();
            
            for (UserAchievementEntity userAchievement : newlyUnlocked) {
                AchievementEntity achievementEntity = achievementDao.getAchievementByIdSync(
                        userAchievement.getAchievementId());
                
                if (achievementEntity != null) {
                    Achievement achievement = achievementEntity.toModel();
                    achievement.setUnlocked(true);
                    achievement.setProgress(userAchievement.getCurrentProgress());
                    
                    unlockedAchievements.add(achievement);
                    
                    // Отправляем уведомление
                    sendAchievementNotification(achievement);
                }
            }
            
            // Обновляем LiveData с новыми достижениями
            newlyUnlockedAchievements.postValue(unlockedAchievements);
            
            // Помечаем достижения как уведомленные
            userAchievementDao.markAllAsNotified(username);
        }
    }
    
    /**
     * Отправляет уведомление о полученном достижении
     */
    private void sendAchievementNotification(@NonNull Achievement achievement) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("OPEN_ACHIEVEMENTS", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_achievement_notification)
                .setContentTitle("Новое достижение разблокировано!")
                .setContentText(achievement.getTitle() + ": " + achievement.getDescription())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        
        int notificationId = NOTIFICATION_ID_BASE + achievement.getId().hashCode();
        notificationManager.notify(notificationId, builder.build());
    }
    
    /**
     * Возвращает LiveData со списком недавно разблокированных достижений
     * для обновления UI
     */
    public LiveData<List<Achievement>> getNewlyUnlockedAchievements() {
        return newlyUnlockedAchievements;
    }
    
    /**
     * Возвращает список всех достижений пользователя (с прогрессом)
     */
    public LiveData<List<Achievement>> getUserAchievements(String username) {
        MutableLiveData<List<Achievement>> result = new MutableLiveData<>(new ArrayList<>());
        
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Achievement> achievements = new ArrayList<>();
            
            // Получаем все достижения
            List<AchievementEntity> allAchievements = achievementDao.getAllAchievementsSync();
            
            // Получаем прогресс пользователя
            List<UserAchievementEntity> userAchievements = userAchievementDao.getUserAchievementsSync(username);
            Map<String, UserAchievementEntity> userAchievementMap = new HashMap<>();
            
            for (UserAchievementEntity userAchievement : userAchievements) {
                userAchievementMap.put(userAchievement.getAchievementId(), userAchievement);
            }
            
            // Объединяем данные
            for (AchievementEntity achievementEntity : allAchievements) {
                Achievement achievement = achievementEntity.toModel();
                
                UserAchievementEntity userAchievement = userAchievementMap.get(achievement.getId());
                if (userAchievement != null) {
                    achievement.setUnlocked(userAchievement.isUnlocked());
                    achievement.setProgress(userAchievement.getCurrentProgress());
                }
                
                achievements.add(achievement);
            }
            
            result.postValue(achievements);
        });
        
        return result;
    }
    
    /**
     * Возвращает общее количество очков и уровень пользователя
     */
    public LiveData<UserEntity> getUserStats(String username) {
        return userDao.getUserByUsername(username);
    }
    
    /**
     * Рассчитывает опыт, необходимый для следующего уровня
     */
    public int getExperienceForNextLevel(int currentLevel) {
        return currentLevel * 100;
    }
    
    /**
     * Возвращает прогресс до следующего уровня в процентах
     */
    public float getLevelProgressPercentage(UserEntity user) {
        int currentExp = user.getExperience();
        int currentLevel = user.getLevel();
        int expForCurrentLevel = (currentLevel - 1) * 100;
        int expForNextLevel = currentLevel * 100;
        
        int expInCurrentLevel = currentExp - expForCurrentLevel;
        int expRangeForLevel = expForNextLevel - expForCurrentLevel;
        
        return (float) expInCurrentLevel / expRangeForLevel;
    }
}