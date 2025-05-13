package com.draker.recmaster.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.draker.recmaster.database.AppDatabase;
import com.draker.recmaster.database.dao.UserDao;
import com.draker.recmaster.database.entity.UserEntity;
import com.draker.recmaster.model.User;

/**
 * Репозиторий для работы с пользователями в базе данных
 */
public class UserRepository {
    
    private static final String TAG = "UserRepository";
    
    private final UserDao userDao;
    
    private static volatile UserRepository INSTANCE;
    
    public static UserRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (UserRepository.class) {
                if (INSTANCE == null) {
                    AppDatabase db = AppDatabase.getInstance(application);
                    INSTANCE = new UserRepository(db.userDao());
                }
            }
        }
        return INSTANCE;
    }
    
    private UserRepository(UserDao userDao) {
        this.userDao = userDao;
        Log.d(TAG, "UserRepository initialized");
    }
    
    /**
     * Получение пользователя по имени
     */
    public LiveData<UserEntity> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
    
    /**
     * Сохранение пользователя в базу данных
     */
    public void insertUser(UserEntity user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
            Log.d(TAG, "Inserted user: " + user.getUsername());
        });
    }
    
    /**
     * Обновление данных пользователя
     */
    public void updateUser(UserEntity user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.update(user);
            Log.d(TAG, "Updated user: " + user.getUsername());
        });
    }
    
    /**
     * Добавление опыта пользователю
     */
    public void addExperience(String username, int amount) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.addExperience(username, amount);
            Log.d(TAG, "Added " + amount + " experience to user: " + username);
        });
    }
    
    /**
     * Преобразование модели User в UserEntity
     */
    public UserEntity userToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setLevel(user.getLevel());
        entity.setExperience(user.getExperience());
        entity.setPreferredGenres(user.getPreferredGenres());
        entity.setAvatarUri(user.getAvatarUri());
        return entity;
    }
    
    /**
     * Преобразование UserEntity в модель User
     */
    public User entityToUser(UserEntity entity) {
        User user = new User();
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setLevel(entity.getLevel());
        user.setExperience(entity.getExperience());
        user.setPreferredGenres(entity.getPreferredGenres());
        user.setAvatarUri(entity.getAvatarUri());
        return user;
    }
    
    /**
     * Проверка, существует ли пользователь с указанным именем
     */
    public void checkUserExists(String username, UserExistsCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserEntity user = userDao.getUserByUsernameSync(username);
            boolean exists = user != null;
            callback.onResult(exists);
        });
    }
    
    /**
     * Проверка, существует ли пользователь с указанным email
     */
    public void checkEmailExists(String email, UserExistsCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserEntity user = userDao.getUserByEmailSync(email);
            boolean exists = user != null;
            callback.onResult(exists);
        });
    }
    
    /**
     * Интерфейс для обратного вызова проверки существования пользователя
     */
    public interface UserExistsCallback {
        void onResult(boolean exists);
    }
    
    /**
     * Очистка всех данных пользователей
     */
    public void clearAllData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.deleteAll();
            Log.d(TAG, "All user data cleared");
        });
    }
}