package com.draker.recmaster.model;

import android.text.TextUtils;
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
        if (TextUtils.isEmpty(releaseDate)) {
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
        
        if (TextUtils.isEmpty(mood)) {
            return true;
        }
        
        if (genreIds == null || genreIds.isEmpty()) {
            return false;
        }
        
        // Примерное соответствие жанров настроениям
        switch (mood.toLowerCase()) {
            case "happy":
                return genreIds.contains(35) || genreIds.contains(10751); // Comedy, Family
            case "sad":
                return genreIds.contains(18); // Drama
            case "excited":
                return genreIds.contains(28) || genreIds.contains(12) || genreIds.contains(878); // Action, Adventure, Science Fiction
            case "relaxed":
                return genreIds.contains(99) || genreIds.contains(36); // Documentary, History
            default:
                return true;
        }
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