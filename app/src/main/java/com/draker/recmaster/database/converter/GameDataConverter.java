package com.draker.recmaster.database.converter;

import androidx.room.TypeConverter;

import com.draker.recmaster.model.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Конвертер для преобразования данных игр в JSON-строку и обратно для хранения в базе данных
 */
public class GameDataConverter {

    private static final Gson gson = new Gson();

    // Конвертеры для Genre
    @TypeConverter
    public static String fromGenreList(List<Game.Genre> value) {
        if (value == null) {
            return null;
        }
        Type type = getGenreListType();
        return gson.toJson(value, type);
    }

    @TypeConverter
    public static List<Game.Genre> toGenreList(String value) {
        if (value == null) {
            return null;
        }
        Type type = getGenreListType();
        return gson.fromJson(value, type);
    }

    // Конвертеры для Platform
    @TypeConverter
    public static String fromPlatformList(List<Game.Platform> value) {
        if (value == null) {
            return null;
        }
        Type type = getPlatformListType();
        return gson.toJson(value, type);
    }

    @TypeConverter
    public static List<Game.Platform> toPlatformList(String value) {
        if (value == null) {
            return null;
        }
        Type type = getPlatformListType();
        return gson.fromJson(value, type);
    }

    // Конвертеры для Developer
    @TypeConverter
    public static String fromDeveloperList(List<Game.Developer> value) {
        if (value == null) {
            return null;
        }
        Type type = getDeveloperListType();
        return gson.toJson(value, type);
    }

    @TypeConverter
    public static List<Game.Developer> toDeveloperList(String value) {
        if (value == null) {
            return null;
        }
        Type type = getDeveloperListType();
        return gson.fromJson(value, type);
    }

    // Конвертеры для Publisher
    @TypeConverter
    public static String fromPublisherList(List<Game.Publisher> value) {
        if (value == null) {
            return null;
        }
        Type type = getPublisherListType();
        return gson.toJson(value, type);
    }

    @TypeConverter
    public static List<Game.Publisher> toPublisherList(String value) {
        if (value == null) {
            return null;
        }
        Type type = getPublisherListType();
        return gson.fromJson(value, type);
    }

    // Конвертеры для Store
    @TypeConverter
    public static String fromStoreList(List<Game.Store> value) {
        if (value == null) {
            return null;
        }
        Type type = getStoreListType();
        return gson.toJson(value, type);
    }

    @TypeConverter
    public static List<Game.Store> toStoreList(String value) {
        if (value == null) {
            return null;
        }
        Type type = getStoreListType();
        return gson.fromJson(value, type);
    }

    // Методы для получения TypeToken
    public static Type getGenreListType() {
        return new TypeToken<List<Game.Genre>>() {}.getType();
    }

    public static Type getPlatformListType() {
        return new TypeToken<List<Game.Platform>>() {}.getType();
    }

    public static Type getDeveloperListType() {
        return new TypeToken<List<Game.Developer>>() {}.getType();
    }

    public static Type getPublisherListType() {
        return new TypeToken<List<Game.Publisher>>() {}.getType();
    }

    public static Type getStoreListType() {
        return new TypeToken<List<Game.Store>>() {}.getType();
    }
}