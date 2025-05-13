package com.draker.recmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.draker.recmaster.database.entity.UserAchievementEntity;

import java.util.List;

/**
 * Data Access Object для работы с прогрессом достижений пользователя
 */
@Dao
public interface UserAchievementDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserAchievementEntity userAchievement);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserAchievementEntity> userAchievements);
    
    @Update
    void update(UserAchievementEntity userAchievement);
    
    @Query("SELECT * FROM user_achievements WHERE username = :username AND achievementId = :achievementId")
    UserAchievementEntity getUserAchievementSync(String username, String achievementId);
    
    @Query("SELECT * FROM user_achievements WHERE username = :username")
    LiveData<List<UserAchievementEntity>> getUserAchievements(String username);
    
    @Query("SELECT * FROM user_achievements WHERE username = :username")
    List<UserAchievementEntity> getUserAchievementsSync(String username);
    
    @Query("SELECT * FROM user_achievements WHERE username = :username AND unlocked = 1")
    LiveData<List<UserAchievementEntity>> getUnlockedAchievements(String username);
    
    @Query("SELECT * FROM user_achievements WHERE username = :username AND unlocked = 1 AND notified = 0")
    List<UserAchievementEntity> getNewlyUnlockedAchievementsSync(String username);
    
    @Query("UPDATE user_achievements SET notified = 1 WHERE username = :username AND unlocked = 1")
    void markAllAsNotified(String username);
    
    @Query("SELECT COUNT(*) FROM user_achievements WHERE username = :username AND unlocked = 1")
    int getUnlockedAchievementCount(String username);
    
    /**
     * Обновляет прогресс достижения пользователя
     */
    @Transaction
    default boolean updateProgress(String username, String achievementId, int amount, int threshold) {
        UserAchievementEntity entity = getUserAchievementSync(username, achievementId);
        
        if (entity == null) {
            entity = new UserAchievementEntity(username, achievementId);
        }
        
        boolean wasUnlocked = entity.incrementProgress(amount, threshold);
        
        insert(entity);
        
        return wasUnlocked;
    }
    
    @Query("DELETE FROM user_achievements WHERE username = :username")
    void deleteAllForUser(String username);
    
    @Query("DELETE FROM user_achievements")
    void deleteAll();
}