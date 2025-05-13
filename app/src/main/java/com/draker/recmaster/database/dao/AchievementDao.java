package com.draker.recmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.draker.recmaster.database.entity.AchievementEntity;

import java.util.List;

/**
 * Data Access Object для работы с достижениями
 */
@Dao
public interface AchievementDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AchievementEntity achievement);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AchievementEntity> achievements);
    
    @Update
    void update(AchievementEntity achievement);
    
    @Delete
    void delete(AchievementEntity achievement);
    
    @Query("SELECT * FROM achievements WHERE id = :id")
    LiveData<AchievementEntity> getAchievementById(String id);
    
    @Query("SELECT * FROM achievements WHERE id = :id")
    AchievementEntity getAchievementByIdSync(String id);
    
    @Query("SELECT * FROM achievements")
    LiveData<List<AchievementEntity>> getAllAchievements();
    
    @Query("SELECT * FROM achievements")
    List<AchievementEntity> getAllAchievementsSync();
    
    @Query("SELECT * FROM achievements WHERE achievementType = :type")
    List<AchievementEntity> getAchievementsByTypeSync(String type);
    
    @Query("DELETE FROM achievements")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM achievements")
    int getAchievementCount();
}