package com.draker.recmaster.ui.achievements;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.draker.recmaster.database.entity.UserEntity;
import com.draker.recmaster.model.Achievement;
import com.draker.recmaster.service.GamificationService;

import java.util.List;

/**
 * ViewModel для экрана достижений
 */
public class AchievementsViewModel extends AndroidViewModel {
    
    private final GamificationService gamificationService;
    
    public AchievementsViewModel(@NonNull Application application) {
        super(application);
        gamificationService = GamificationService.getInstance(application);
    }
    
    /**
     * Получает все достижения пользователя
     */
    public LiveData<List<Achievement>> getUserAchievements(String username) {
        return gamificationService.getUserAchievements(username);
    }
    
    /**
     * Получает информацию о пользователе (уровень, опыт)
     */
    public LiveData<UserEntity> getUserStats(String username) {
        return gamificationService.getUserStats(username);
    }
    
    /**
     * Получает список новых разблокированных достижений
     */
    public LiveData<List<Achievement>> getNewlyUnlockedAchievements() {
        return gamificationService.getNewlyUnlockedAchievements();
    }
    
    /**
     * Рассчитывает прогресс до следующего уровня в процентах (0.0 - 1.0)
     */
    public float getLevelProgressPercentage(UserEntity user) {
        return gamificationService.getLevelProgressPercentage(user);
    }
}