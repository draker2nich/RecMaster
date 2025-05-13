package com.draker.recmaster.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий параметры фильтрации для рекомендаций
 */
public class RecommendationFilter {
    
    // Константы для временных диапазонов
    public static final int DURATION_ANY = 0;
    public static final int DURATION_SHORT = 90;    // До 1.5 часа
    public static final int DURATION_MEDIUM = 120;  // До 2 часов
    public static final int DURATION_LONG = 180;    // До 3 часов
    
    // Константы для настроений
    public static final String MOOD_ANY = "";
    public static final String MOOD_HAPPY = "happy"; 
    public static final String MOOD_SAD = "sad";
    public static final String MOOD_EXCITED = "excited";
    public static final String MOOD_RELAXED = "relaxed";
    
    private String mood = MOOD_ANY;
    private int maxDuration = DURATION_ANY;
    private List<Integer> selectedGenreIds = new ArrayList<>();
    
    // Создание пустого фильтра без ограничений
    public RecommendationFilter() {
    }
    
    // Создание фильтра с указанными параметрами
    public RecommendationFilter(String mood, int maxDuration, List<Integer> selectedGenreIds) {
        this.mood = mood;
        this.maxDuration = maxDuration;
        if (selectedGenreIds != null) {
            this.selectedGenreIds = new ArrayList<>(selectedGenreIds);
        }
    }
    
    // Getters и Setters
    public String getMood() {
        return mood;
    }
    
    public void setMood(String mood) {
        this.mood = mood;
    }
    
    public int getMaxDuration() {
        return maxDuration;
    }
    
    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }
    
    public List<Integer> getSelectedGenreIds() {
        return selectedGenreIds;
    }
    
    public void setSelectedGenreIds(List<Integer> selectedGenreIds) {
        this.selectedGenreIds = selectedGenreIds != null ? 
                new ArrayList<>(selectedGenreIds) : new ArrayList<>();
    }
    
    public void addGenreId(int genreId) {
        if (!selectedGenreIds.contains(genreId)) {
            selectedGenreIds.add(genreId);
        }
    }
    
    public void removeGenreId(int genreId) {
        selectedGenreIds.remove(Integer.valueOf(genreId));
    }
    
    public void clearGenres() {
        selectedGenreIds.clear();
    }
    
    /**
     * Проверяет, соответствует ли фильм всем заданным критериям
     */
    public boolean matches(Movie movie) {
        if (movie == null) {
            return false;
        }
        
        // Проверка по настроению
        boolean moodMatches = mood == null || mood.isEmpty() || movie.matchesMood(mood);
        
        // Проверка по длительности
        boolean durationMatches = maxDuration <= 0 || movie.matchesDuration(maxDuration);
        
        // Проверка по жанрам
        boolean genresMatch = selectedGenreIds.isEmpty() || movie.matchesAnyGenre(selectedGenreIds);
        
        return moodMatches && durationMatches && genresMatch;
    }
    
    /**
     * Фильтрует список фильмов по заданным критериям
     */
    public List<Movie> filter(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Movie> filtered = new ArrayList<>();
        for (Movie movie : movies) {
            if (matches(movie)) {
                filtered.add(movie);
            }
        }
        
        return filtered;
    }
    
    @Override
    public String toString() {
        return "RecommendationFilter{" +
                "mood='" + mood + '\'' +
                ", maxDuration=" + maxDuration +
                ", selectedGenreIds=" + selectedGenreIds +
                '}';
    }
}