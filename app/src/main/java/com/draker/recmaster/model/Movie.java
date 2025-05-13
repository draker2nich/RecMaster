package com.draker.recmaster.model;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Модель данных для представления фильма из TMDB API
 */
public class Movie implements Serializable {
    private static final String TAG = "Movie";

    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private float voteAverage;
    private int voteCount;
    private String releaseDate;
    private List<Integer> genreIds;
    private float popularity;
    private boolean adult;
    private int runtime; // продолжительность фильма в минутах

    // Базовый URL для изображений
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342";
    private static final String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780";

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    // Получение полного URL для постера
    public String getPosterUrl() {
        if (posterPath != null && !posterPath.isEmpty()) {
            return POSTER_BASE_URL + posterPath;
        }
        return null;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    // Получение полного URL для фона
    public String getBackdropUrl() {
        if (backdropPath != null && !backdropPath.isEmpty()) {
            return BACKDROP_BASE_URL + backdropPath;
        }
        return null;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Возвращает отформатированную дату релиза в локализованном формате
     */
    public String getFormattedReleaseDate() {
        if (releaseDate == null || releaseDate.isEmpty()) {
            return "";
        }
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
            Date date = inputFormat.parse(releaseDate);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + releaseDate, e);
        }
        
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }
    
    public int getRuntime() {
        return runtime;
    }
    
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
    
    /**
     * Возвращает отформатированную строку времени в формате "2ч 15м"
     */
    public String getFormattedRuntime() {
        if (runtime <= 0) {
            return "";
        }
        
        int hours = runtime / 60;
        int minutes = runtime % 60;
        
        if (hours > 0) {
            return hours + "ч " + minutes + "м";
        } else {
            return minutes + "м";
        }
    }
    
    /**
     * Получает строковое представление жанров фильма
     */
    public String getGenresString(Map<Integer, String> genreMap) {
        if (genreIds == null || genreIds.isEmpty() || genreMap == null || genreMap.isEmpty()) {
            return "";
        }
        
        StringBuilder genres = new StringBuilder();
        for (Integer genreId : genreIds) {
            String genreName = genreMap.get(genreId);
            if (genreName != null) {
                if (genres.length() > 0) {
                    genres.append(", ");
                }
                genres.append(genreName);
            }
        }
        
        return genres.toString();
    }
    
    /**
     * Определяет, соответствует ли фильм определенному настроению
     */
    public boolean matchesMood(String mood) {
        // Это очень упрощенная реализация - в реальном приложении
        // могла бы быть более сложная логика определения настроения
        // на основе жанров, описания и т.д.
        
        if (mood == null || mood.isEmpty()) {
            return true;
        }
        
        if (genreIds == null || genreIds.isEmpty()) {
            return false;
        }
        
        // Примерное соответствие жанров настроениям
        if (mood.equalsIgnoreCase("happy")) {
            return genreIds.contains(35) || genreIds.contains(10751); // Comedy, Family
        } else if (mood.equalsIgnoreCase("sad")) {
            return genreIds.contains(18); // Drama 
        } else if (mood.equalsIgnoreCase("excited")) {
            return genreIds.contains(28) || genreIds.contains(12) || genreIds.contains(878); // Action, Adventure, Science Fiction
        } else if (mood.equalsIgnoreCase("relaxed")) {
            return genreIds.contains(99) || genreIds.contains(36); // Documentary, History
        } else {
            return true;
        }
    }
    
    /**
     * Определяет, соответствует ли фильм определенному диапазону времени
     * @param maxMinutes максимальная длительность в минутах
     * @return true, если фильм укладывается в указанное время
     */
    public boolean matchesDuration(int maxMinutes) {
        // Если продолжительность не указана или задана нулевая,
        // то считаем фильм подходящим по любому временному критерию
        if (runtime <= 0 || maxMinutes <= 0) {
            return true;
        }
        
        return runtime <= maxMinutes;
    }

    /**
     * Проверяет, содержит ли фильм указанный жанр
     * @param genreId ID жанра
     * @return true, если фильм принадлежит указанному жанру
     */
    public boolean hasGenre(int genreId) {
        return genreIds != null && genreIds.contains(genreId);
    }
    
    /**
     * Проверяет, соответствует ли фильм всем указанным жанрам
     * @param selectedGenreIds список ID выбранных жанров
     * @return true, если фильм содержит все указанные жанры
     */
    public boolean matchesAllGenres(List<Integer> selectedGenreIds) {
        if (selectedGenreIds == null || selectedGenreIds.isEmpty()) {
            return true;
        }
        
        if (genreIds == null || genreIds.isEmpty()) {
            return false;
        }
        
        return genreIds.containsAll(selectedGenreIds);
    }
    
    /**
     * Проверяет, соответствует ли фильм хотя бы одному из указанных жанров
     * @param selectedGenreIds список ID выбранных жанров
     * @return true, если фильм содержит хотя бы один из указанных жанров
     */
    public boolean matchesAnyGenre(List<Integer> selectedGenreIds) {
        if (selectedGenreIds == null || selectedGenreIds.isEmpty()) {
            return true;
        }
        
        if (genreIds == null || genreIds.isEmpty()) {
            return false;
        }
        
        for (Integer genreId : selectedGenreIds) {
            if (genreIds.contains(genreId)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                '}';
    }
}