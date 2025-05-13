package com.draker.recmaster.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.draker.recmaster.database.converter.StringListConverter;

import java.util.List;

/**
 * Entity класс, представляющий пользователя в базе данных Room
 */
@Entity(tableName = "users")
@TypeConverters(StringListConverter.class)
public class UserEntity {
    
    @PrimaryKey
    @NonNull
    private String username;
    
    private String email;
    private String password; // В реальном приложении пароль должен быть хеширован
    private int level;
    private int experience;
    private List<String> preferredGenres;
    private String avatarUri;
    
    // Конструктор по умолчанию, обязателен для Room
    public UserEntity() {
    }
    
    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<String> getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(List<String> preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}