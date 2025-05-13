package com.draker.recmaster.model;

/**
 * Модель данных для достижений
 */
public class Achievement {
    private String id;              // Уникальный идентификатор достижения
    private String title;           // Название достижения
    private String description;     // Описание достижения
    private String iconResourceName; // Имя ресурса для иконки
    private int pointsReward;       // Награда в очках опыта
    private AchievementType type;   // Тип достижения
    private int threshold;          // Порог для получения достижения (например, 5 фильмов)
    private boolean unlocked;       // Разблокировано ли достижение пользователем
    private int currentProgress;    // Текущий прогресс пользователя

    // Типы достижений
    public enum AchievementType {
        WATCH_COUNT,         // Общее количество просмотренных фильмов
        GENRE_WATCH_COUNT,   // Количество фильмов определенного жанра
        RATED_MOVIES,        // Количество оцененных фильмов
        DAILY_STREAK,        // Дни подряд с активностью
        LEVEL_REACHED,       // Достигнут определенный уровень
        FIRST_MOVIE,         // Первый фильм
        CUSTOM              // Кастомное достижение
    }

    // Конструктор по умолчанию
    public Achievement() {
    }

    // Конструктор с параметрами для создания нового достижения
    public Achievement(String id, String title, String description, String iconResourceName, 
                      int pointsReward, AchievementType type, int threshold) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconResourceName = iconResourceName;
        this.pointsReward = pointsReward;
        this.type = type;
        this.threshold = threshold;
        this.unlocked = false;
        this.currentProgress = 0;
    }

    // Методы для работы с прогрессом достижения
    public void incrementProgress() {
        this.currentProgress++;
        checkUnlock();
    }

    public void incrementProgress(int amount) {
        this.currentProgress += amount;
        checkUnlock();
    }

    private void checkUnlock() {
        if (!unlocked && currentProgress >= threshold) {
            unlocked = true;
        }
    }

    public void setProgress(int progress) {
        this.currentProgress = progress;
        checkUnlock();
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public AchievementType getType() {
        return type;
    }

    public void setType(AchievementType type) {
        this.type = type;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public float getProgressPercentage() {
        if (threshold == 0) return 0f;
        return (float) currentProgress / threshold;
    }
}