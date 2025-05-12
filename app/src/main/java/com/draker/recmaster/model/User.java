package com.draker.recmaster.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель данных пользователя
 */
public class User {
    private String username;
    private String email;
    private String password;
    private int level;
    private int experience;
    private List<String> preferredGenres;
    private String avatarUri;

    public User() {
        this.preferredGenres = new ArrayList<>();
        this.level = 1;
        this.experience = 0;
    }

    public User(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Геттеры и сеттеры
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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
        // Расчет уровня на основе опыта
        // Простая формула: каждые 100 опыта = 1 уровень
        this.level = (experience / 100) + 1;
    }

    public void addExperience(int amount) {
        this.experience += amount;
        // Обновляем уровень
        this.level = (this.experience / 100) + 1;
    }

    public List<String> getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(List<String> preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    public void addPreferredGenre(String genre) {
        if (!this.preferredGenres.contains(genre)) {
            this.preferredGenres.add(genre);
        }
    }

    public void removePreferredGenre(String genre) {
        this.preferredGenres.remove(genre);
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}