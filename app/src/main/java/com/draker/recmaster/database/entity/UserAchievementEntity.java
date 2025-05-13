package com.draker.recmaster.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.TypeConverters;

import com.draker.recmaster.database.converter.DateConverter;

import java.util.Date;

/**
 * Entity класс для хранения информации о прогрессе пользователя по достижениям
 */
@Entity(
    tableName = "user_achievements",
    primaryKeys = {"username", "achievementId"},
    foreignKeys = {
        @ForeignKey(
            entity = UserEntity.class,
            parentColumns = "username",
            childColumns = "username",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = AchievementEntity.class,
            parentColumns = "id",
            childColumns = "achievementId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("username"),
        @Index("achievementId")
    }
)
@TypeConverters(DateConverter.class)
public class UserAchievementEntity {
    
    @NonNull
    private String username;
    
    @NonNull
    private String achievementId;
    
    private int currentProgress;
    private boolean unlocked;
    private Date unlockedDate;
    private boolean notified; // было ли отправлено уведомление о разблокировке
    
    // Конструктор по умолчанию, обязателен для Room
    public UserAchievementEntity() {
        this.notified = false;
    }
    
    // Конструктор с параметрами
    @androidx.room.Ignore
    public UserAchievementEntity(@NonNull String username, @NonNull String achievementId) {
        this.username = username;
        this.achievementId = achievementId;
        this.currentProgress = 0;
        this.unlocked = false;
        this.notified = false;
    }
    
    // Геттеры и сеттеры
    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(@NonNull String achievementId) {
        this.achievementId = achievementId;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
        if (unlocked && unlockedDate == null) {
            this.unlockedDate = new Date();
        }
    }

    public Date getUnlockedDate() {
        return unlockedDate;
    }

    public void setUnlockedDate(Date unlockedDate) {
        this.unlockedDate = unlockedDate;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
    
    /**
     * Увеличивает прогресс на указанное количество и проверяет, разблокировано ли достижение
     * 
     * @param amount количество для увеличения
     * @param threshold порог для разблокировки
     * @return true если достижение было разблокировано в результате этого действия
     */
    public boolean incrementProgress(int amount, int threshold) {
        boolean wasUnlocked = unlocked;
        currentProgress += amount;
        
        if (!unlocked && currentProgress >= threshold) {
            unlocked = true;
            unlockedDate = new Date();
        }
        
        return !wasUnlocked && unlocked;
    }
}