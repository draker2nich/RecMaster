package com.draker.recmaster.database.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Type Converter для конвертации между List<Integer> и String в базе данных
 */
public class GenreIdsConverter {
    
    private static final Gson gson = new Gson();
    
    @TypeConverter
    public static List<Integer> fromString(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromList(List<Integer> list) {
        if (list == null) {
            return null;
        }
        
        return gson.toJson(list);
    }
}