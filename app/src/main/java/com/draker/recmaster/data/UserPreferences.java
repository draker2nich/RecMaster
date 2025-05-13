package com.draker.recmaster.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.draker.recmaster.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения данных пользователя в SharedPreferences
 */
public class UserPreferences {
    private static final String PREFS_NAME = "RecMasterPrefs";
    private static final String KEY_USER_LOGGED_IN = "user_logged_in";
    private static final String KEY_USER_DATA = "user_data";

    private final SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Сохраняет данные пользователя
     */
    public void saveUser(User user) {
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("username", user.getUsername());
            userJson.put("email", user.getEmail());
            userJson.put("password", user.getPassword());
            userJson.put("level", user.getLevel());
            userJson.put("experience", user.getExperience());
            userJson.put("avatarUri", user.getAvatarUri() != null ? user.getAvatarUri() : "");

            // Сохраняем предпочитаемые жанры
            JSONArray genresArray = new JSONArray();
            for (String genre : user.getPreferredGenres()) {
                genresArray.put(genre);
            }
            userJson.put("preferredGenres", genresArray);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_USER_DATA, userJson.toString());
            editor.putBoolean(KEY_USER_LOGGED_IN, true);
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает данные пользователя
     */
    public User loadUser() {
        String userJson = preferences.getString(KEY_USER_DATA, null);
        if (userJson != null) {
            try {
                JSONObject jsonObject = new JSONObject(userJson);
                User user = new User();
                user.setUsername(jsonObject.getString("username"));
                user.setEmail(jsonObject.getString("email"));
                user.setPassword(jsonObject.getString("password"));
                user.setLevel(jsonObject.getInt("level"));
                user.setExperience(jsonObject.getInt("experience"));
                
                String avatarUri = jsonObject.optString("avatarUri", "");
                if (!avatarUri.isEmpty()) {
                    user.setAvatarUri(avatarUri);
                }

                // Загружаем предпочитаемые жанры
                JSONArray genresArray = jsonObject.getJSONArray("preferredGenres");
                List<String> genres = new ArrayList<>();
                for (int i = 0; i < genresArray.length(); i++) {
                    genres.add(genresArray.getString(i));
                }
                user.setPreferredGenres(genres);

                return user;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Проверяет, вошел ли пользователь в систему
     */
    public boolean isUserLoggedIn() {
        return preferences.getBoolean(KEY_USER_LOGGED_IN, false);
    }

    /**
     * Выход пользователя
     */
    public void logoutUser() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_USER_LOGGED_IN, false);
        editor.apply();
    }

    /**
     * Обновляет только определенные поля пользователя
     */
    public void updateUserField(String field, String value) {
        User user = loadUser();
        if (user != null) {
            switch (field) {
                case "username":
                    user.setUsername(value);
                    break;
                case "email":
                    user.setEmail(value);
                    break;
                case "avatarUri":
                    user.setAvatarUri(value);
                    break;
            }
            saveUser(user);
        }
    }
    
    /**
     * Получить имя пользователя текущего активного аккаунта
     */
    public String getUsername() {
        User user = loadUser();
        return user != null ? user.getUsername() : "";
    }
}