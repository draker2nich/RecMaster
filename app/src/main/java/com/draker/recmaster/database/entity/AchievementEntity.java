package com.draker.recmaster.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.draker.recmaster.model.Achievement;

/**
 * Entity класс для хранения информации о доступных достижениях
 */
@Entity(tableName = "achievements", indices = {@Index(value = {"id"}, unique = true)})
public class AchievementEntity {
    
    @PrimaryKey
    @NonNull
    private String id;
    
    private String title;
    private String description;
    private String iconResourceName;
    private int pointsReward;
    private String achievementType; // Хранится как строка, конвертируется при чтении
    private int threshold;
    
    // Конструктор по умолчанию (необходим для Room)
    public AchievementEntity() {
    }
    
    /**
     * Конструктор из модели Achievement
     */
    public AchievementEntity(Achievement achievement) {
        this.id = achievement.getId();
        this.title = achievement.getTitle();
        this.description = achievement.getDescription();
        this.iconResourceName = achievement.getIconResourceName();
        this.pointsReward = achievement.getPointsReward();
        this.achievementType = achievement.getType().name();
        this.threshold = achievement.getThreshold();
    }
    
    /**
     * Преобразует сущность в модель Achievement
     */
    public Achievement toModel() {
        Achievement achievement = new Achievement();
        achievement.setId(id);
        achievement.setTitle(title);
        achievement.setDescription(description);
        achievement.setIconResourceName(iconResourceName);
        achievement.setPointsReward(pointsReward);
        achievement.setType(Achievement.AchievementType.valueOf(achievementType));
        achievement.setThreshold(threshold);
        return achievement;
    }
    
    // Геттеры и сеттеры
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconResourceName() {
        return iconResourceName;
    }

    public void setIconResourceName(String iconResourceName) {
        this.iconResourceName = iconResourceName;
    }

    public int getPointsReward() {
        return pointsReward;
    }

    public void setPointsReward(int pointsReward) {
        this.pointsReward = pointsReward;
    }

    public String getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}