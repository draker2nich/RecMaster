package com.draker.recmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.draker.recmaster.database.entity.UserEntity;

/**
 * Data Access Object для операций с таблицей пользователей
 */
@Dao
public interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);
    
    @Update
    void update(UserEntity user);
    
    @Delete
    void delete(UserEntity user);
    
    @Query("SELECT * FROM users WHERE username = :username")
    LiveData<UserEntity> getUserByUsername(String username);
    
    @Query("SELECT * FROM users WHERE username = :username")
    UserEntity getUserByUsernameSync(String username);
    
    @Query("SELECT * FROM users WHERE email = :email")
    UserEntity getUserByEmailSync(String email);
    
    @Query("UPDATE users SET experience = experience + :amount, level = (experience + :amount) / 100 + 1 WHERE username = :username")
    void addExperience(String username, int amount);
    
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
    
    @Query("DELETE FROM users")
    void deleteAll();
}