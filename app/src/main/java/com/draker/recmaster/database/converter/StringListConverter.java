package com.draker.recmaster.database.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Конвертер для преобразования списка строк в JSON-строку и обратно для хранения в базе данных
 */
public class StringListConverter {

    @TypeConverter
    public static String fromStringList(List<String> value) {
        if (value == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.toJson(value, type);
    }

    @TypeConverter
    public static List<String> toStringList(String value) {
        if (value == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, type);
    }
}